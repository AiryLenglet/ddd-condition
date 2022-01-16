package conditions.spring;

import conditions.core.event.EventBus;
import conditions.core.event.TaskEvent;
import conditions.core.event.approval.AskedChangeEvent;
import conditions.core.event.approval.ConditionAcceptedEvent;
import conditions.core.event.condition.ConditionDiscardedEvent;
import conditions.core.event.condition.ConditionOpenedEvent;
import conditions.core.event.fulfillment.*;
import conditions.core.event_handler.*;
import conditions.core.model.Condition;
import conditions.core.model.ConditionSetupTask;
import conditions.core.model.FulfillmentTask;
import conditions.core.model.Task;
import conditions.core.repository.ConditionRepository;
import conditions.core.repository.FulfillmentRepository;
import conditions.core.repository.TaskRepository;
import conditions.spring.controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.function.BiFunction;

@Service
public class ConditionWorkflowConfig implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConditionWorkflowConfig.class);

    @Autowired
    private ConditionRepository conditionRepository;
    @Autowired
    private FulfillmentRepository fulfillmentRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EventBus eventBus;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

        /* CONDITION */
        this.eventBus.subscribe(ConditionDiscardedEvent.class, event -> {
            final var fulfillments = fulfillmentRepository.findAll((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("conditionId"), event.conditionId()));
            for (final var fulfillment : fulfillments) {
                fulfillment.cancel();
                fulfillmentRepository.save(fulfillment);
            }
        });

        /* FULFILLMENT */
        this.eventBus.subscribe(FulfillmentCancelledEvent.class, event -> {
            final var tasks = taskRepository.findAll((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("fulffilmentId"), event.fulfillmentId()));
            for (final var task : tasks) {
                task.cancel();
                taskRepository.save(task);
            }
        });

        /* APPROVAL */
        this.eventBus.subscribe(ConditionAcceptedEvent.class, new ConditionAcceptedEventHandler(conditionRepository));
        this.eventBus.subscribe(ConditionOpenedEvent.class, new ConditionOpenedEventHandler(conditionRepository, fulfillmentRepository));
        this.eventBus.subscribe(
                AskedChangeEvent.class,
                nextTask((c, e) -> new ConditionSetupTask(e.conditionId(), e.fulfillmentId(), c.getOwner().getPid(), e.taskId()))
        );

        /* FULFILLMENT */
        this.eventBus.subscribe(FulfillmentOpenedEvent.class, new FulfillmentOpenedEventHandler(conditionRepository, this.taskRepository));

        this.eventBus.subscribe(
                FulfillmentVerificationAskedForChangeEvent.class,
                nextTask((c, e) -> new FulfillmentTask(c.getConditionId(), e.fulfillmentId(), c.getImposer().getPid()))
        );

        this.eventBus.subscribe(FulfillmentVerifiedEvent.class, new FulfillmentVerifiedEventHandler(
                conditionRepository,
                fulfillmentRepository,
                taskRepository
        ));

        this.eventBus.subscribe(FulfillmentReviewedEvent.class, event -> {
            final var fulfillment = fulfillmentRepository.findById(event.fulfillmentId());
            fulfillment.finished();
            fulfillmentRepository.save(fulfillment);
        });

        this.eventBus.subscribe(FulfillmentFinishedEvent.class, event -> {
            final var condition = conditionRepository.findById(event.conditionId());
            if (condition.isRecurring()) {
                return;
            }
            condition.close();
            conditionRepository.save(condition);
        });
    }

    private <T extends TaskEvent> NextTaskEventHandler nextTask(BiFunction<Condition, T, Task> taskProducer) {
        return new NextTaskEventHandler(this.conditionRepository, this.taskRepository, taskProducer);
    }

}
