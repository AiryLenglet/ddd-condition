package conditions.core.event_handler;

import conditions.core.event.EventBus;
import conditions.core.event.TaskEvent;
import conditions.core.event.approval.AskedChangeEvent;
import conditions.core.event.approval.ConditionAcceptedEvent;
import conditions.core.event.condition.ConditionDiscardedEvent;
import conditions.core.event.condition.ConditionOpenedEvent;
import conditions.core.event.fulfillment.*;
import conditions.core.model.*;
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
    private final EventBus eventBus;

    public ConditionWorkflowConfigurer(
            ConditionRepository conditionRepository,
            FulfillmentRepository fulfillmentRepository,
            TaskRepository taskRepository,
            EventBus eventBus
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
                ConditionAcceptedEvent.class,
                event -> {
                    final var condition = condition(event.conditionId());
                    condition.open();
                    this.conditionRepository.save(condition);
                }
        );

        this.eventBus.subscribe(
                ConditionOpenedEvent.class,
                event -> this.fulfillmentRepository.save(condition(event.getConditionId()).startNewFulfillment())
        );

        this.eventBus.subscribe(
                AskedChangeEvent.class,
                nextTask((c, e) -> new ConditionSetupTask(e.conditionId(), e.fulfillmentId(), c.getOwner().getPid(), e.taskId()))
        );

        /* FULFILLMENT */
        this.eventBus.subscribe(
                FulfillmentOpenedEvent.class,
                nextTask((c, e) -> new FulfillmentTask(c.getConditionId(), e.fulfillmentId(), c.getImposer().getPid()))
        );

        this.eventBus.subscribe(
                FulfillmentVerificationAskedForChangeEvent.class,
                nextTask((c, e) -> new FulfillmentTask(c.getConditionId(), e.fulfillmentId(), c.getImposer().getPid()))
        );

        this.eventBus.subscribe(
                FulfillmentVerifiedEvent.class,
                event -> {
                    final var fulfillment = fulfillment(event.fulfillmentId());
                    if (fulfillment.isFulfillmentReviewRequired()) {
                        final var condition = condition(event.conditionId());
                        this.taskRepository.save(new ReviewTask(event.conditionId(), event.fulfillmentId(), condition.getImposer().getPid(), event.taskId()));
                    } else {
                        close(fulfillment);
                    }
                }
        );

        this.eventBus.subscribe(FulfillmentReviewedEvent.class, event -> close(fulfillment(event.fulfillmentId())));

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

    private <T extends TaskEvent> NextTaskEventHandler nextTask(BiFunction<Condition, T, Task> taskProducer) {
        return new NextTaskEventHandler(this.conditionRepository, this.taskRepository, taskProducer);
    }

}
