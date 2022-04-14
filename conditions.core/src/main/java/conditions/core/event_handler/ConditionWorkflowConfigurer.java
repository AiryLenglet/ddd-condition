package conditions.core.event_handler;

import conditions.core.event.DomainEventBus;
import conditions.core.event.Handler;
import conditions.core.event.TaskEvent;
import conditions.core.event.approval.*;
import conditions.core.event.condition.ConditionCreatedEvent;
import conditions.core.event.condition.ConditionDiscardedEvent;
import conditions.core.event.condition.ConditionOpenedEvent;
import conditions.core.event.condition.ConditionSetupedEvent;
import conditions.core.event.fulfillment.*;
import conditions.core.model.*;
import conditions.core.model.task.*;
import conditions.core.repository.ConditionRepository;
import conditions.core.repository.FulfillmentRepository;
import conditions.core.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiFunction;

import static conditions.core.repository.ConditionRepository.Specifications.conditionId;

public class ConditionWorkflowConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConditionWorkflowConfigurer.class);

    private final ConditionRepository conditionRepository;
    private final FulfillmentRepository fulfillmentRepository;
    private final TaskRepository taskRepository;
    private final DomainEventBus eventBus;

    public ConditionWorkflowConfigurer(
            ConditionRepository conditionRepository,
            FulfillmentRepository fulfillmentRepository,
            TaskRepository taskRepository,
            DomainEventBus eventBus
    ) {
        this.conditionRepository = conditionRepository;
        this.fulfillmentRepository = fulfillmentRepository;
        this.taskRepository = taskRepository;
        this.eventBus = eventBus;
    }

    public void configure() {

        LOGGER.info("Starting condition workflow configuration");

        /* CONDITION */
        this.eventBus.subscribe(
                ConditionDiscardedEvent.class,
                event -> fulfillmentRepository.findAll(FulfillmentRepository.Specifications.conditionId(event.conditionId()))
                        .forEach(this::cancel)
        );

        /* FULFILLMENT */
        this.eventBus.subscribe(
                FulfillmentCancelledEvent.class,
                event -> taskRepository.findAll(TaskRepository.Specifications.fulfillmentId(event.fulfillmentId()))
                        .forEach(this::cancel)
        );

        /* APPROVAL */
        this.eventBus.subscribe(
                ConditionCreatedEvent.class,
                event -> this.fulfillmentRepository.save(Fulfillment.createApproval(event.conditionId()))
        );

        this.eventBus.subscribe(
                ApprovalOpenedEvent.class,
                event -> {
                    final var condition = condition(event.conditionId());
                    this.taskRepository.save(new ConditionSetupTask(event.conditionId(), event.fulfillmentId(), condition.getImposer()));
                }
        );

        this.eventBus.subscribe(
                ConditionSetupedEvent.class,
                nextTask((c, e) -> new ApprovalTask(e.conditionId(), e.fulfillmentId(), c.getOwner(), e.taskId()))
        );

        this.eventBus.subscribe(
                ApprovalAcceptedEvent.class,
                event -> {
                    final var approval = fulfillment(event.fulfillmentId());
                    approval.finished();
                    this.fulfillmentRepository.save(approval);
                    final var condition = condition(event.conditionId());
                    condition.open();
                    this.conditionRepository.save(condition);
                }
        );

        this.eventBus.subscribe(
                ConditionOpenedEvent.class,
                event -> this.fulfillmentRepository.save(condition(event.conditionId()).startNewFulfillment())
        );

        this.eventBus.subscribe(
                ApprovalAskedChangeEvent.class,
                nextTask((c, e) -> new ConditionSetupTask(e.conditionId(), e.fulfillmentId(), c.getImposer(), e.taskId()))
        );

        this.eventBus.subscribe(
                ApprovalRejectedEvent.class,
                nextTask((c, e) -> new RefusalReviewTask(e.conditionId(), e.fulfillmentId(), c.getImposer(), e.taskId()))
        );

        this.eventBus.subscribe(
                ChangeReviewEscalatedEvent.class,
                nextTask((c, e) -> new EscalationTask(e.conditionId(), e.fulfillmentId(), c.getSupervisor(), e.taskId()))
        );

        this.eventBus.subscribe(
                RefusalReviewEscalatedEvent.class,
                nextTask((c, e) -> new EscalationTask(e.conditionId(), e.fulfillmentId(), c.getSupervisor(), e.taskId()))
        );

        this.eventBus.subscribe(
                EscalationResolvedEvent.class,
                nextTask((c, e) -> new ConditionSetupTask(e.conditionId(), e.fulfillmentId(), c.getSupervisor(), e.taskId()))
        );

        this.eventBus.subscribe(
                ChangeReviewDiscardedEvent.class,
                event -> cancel(event.conditionId())
        );
        this.eventBus.subscribe(
                RefusalReviewDiscardedEvent.class,
                event -> cancel(event.conditionId())
        );
        this.eventBus.subscribe(
                EscalationDiscardedEvent.class,
                event -> cancel(event.conditionId())
        );

        /* FULFILLMENT */
        this.eventBus.subscribe(
                FulfillmentOpenedEvent.class,
                new Handler<FulfillmentOpenedEvent>() {
                    @Override
                    public void handle(FulfillmentOpenedEvent event) {
                        nextTask((c, e) -> new FulfillmentTask(c.getConditionId(), e.fulfillmentId(), c.getImposer())).handle(new TaskEvent() {
                            @Override
                            public TaskId taskId() {
                                return null;
                            }

                            @Override
                            public FulfillmentId fulfillmentId() {
                                return event.fulfillmentId();
                            }

                            @Override
                            public ConditionId conditionId() {
                                return event.conditionId();
                            }
                        });
                    }
                }
        );

        this.eventBus.subscribe(
                ConditionFulfilledEvent.class,
                nextTask((c, e) -> new VerificationTask(c.getConditionId(), e.fulfillmentId(), c.getImposer(), e.taskId()))
        );

        this.eventBus.subscribe(
                FulfillmentReviewedEvent.class,
                event -> close(fulfillment(event.fulfillmentId()))
        );

        this.eventBus.subscribe(
                FulfillmentVerificationAskedForChangeEvent.class,
                nextTask((c, e) -> new FulfillmentTask(c.getConditionId(), e.fulfillmentId(), c.getImposer()))
        );

        this.eventBus.subscribe(
                FulfillmentVerifiedEvent.class,
                event -> {
                    final var fulfillment = fulfillment(event.fulfillmentId());
                    if (fulfillment.isFulfillmentReviewRequired()) {
                        final var condition = condition(event.conditionId());
                        this.taskRepository.save(new ReviewTask(event.conditionId(), event.fulfillmentId(), condition.getImposer(), event.taskId()));
                    } else {
                        close(fulfillment);
                    }
                }
        );

        this.eventBus.subscribe(FulfillmentFinishedEvent.class, event -> {
            final var condition = condition(event.conditionId());
            if (condition.isRecurring()) {
                return;
            }
            condition.close();
            conditionRepository.save(condition);
        });
    }

    private void cancel(Task task) {
        task.cancel();
        taskRepository.save(task);
    }

    private void cancel(Fulfillment fulfillment) {
        fulfillment.cancel();
        fulfillmentRepository.save(fulfillment);
    }

    private void cancel(Condition condition) {
        condition.cancel();
        conditionRepository.save(condition);
    }

    private void cancel(ConditionId conditionId) {
        cancel(condition(conditionId));
    }

    private void close(Fulfillment fulfillment) {
        fulfillment.finished();
        this.fulfillmentRepository.save(fulfillment);
    }

    private Condition condition(ConditionId conditionId) {
        return conditionRepository.findOne(conditionId(conditionId));
    }

    private Fulfillment fulfillment(FulfillmentId fulfillmentId) {
        return this.fulfillmentRepository.findOne(FulfillmentRepository.Specifications.id(fulfillmentId));
    }

    private <T extends TaskEvent> NextTaskEventHandler<T> nextTask(BiFunction<Condition, T, Task> taskProducer) {
        return new NextTaskEventHandler<>(this.conditionRepository, this.taskRepository, taskProducer);
    }

}
