package conditions.core.event_handler;

import conditions.core.event.fulfillment.FulfillmentVerifiedEvent;
import conditions.core.model.FulfillmentTask;
import conditions.core.repository.ConditionRepository;
import conditions.core.repository.FulfillmentRepository;
import conditions.core.repository.TaskRepository;

public class FulfillmentVerifiedEventHandler extends NextTaskEventHandler<FulfillmentVerifiedEvent> {

    private final FulfillmentRepository fulfillmentRepository;

    public FulfillmentVerifiedEventHandler(
            ConditionRepository conditionRepository,
            FulfillmentRepository fulfillmentRepository,
            TaskRepository taskRepository
    ) {
        super(
                conditionRepository,
                taskRepository,
                (c, e) -> new FulfillmentTask(e.conditionId(), e.fulfillmentId(), c.getImposer().getPid(), e.taskId()));
        this.fulfillmentRepository = fulfillmentRepository;
    }

    @Override
    public void handle(FulfillmentVerifiedEvent event) {
        final var fulfillment = this.fulfillmentRepository.findById(event.fulfillmentId());
        if (fulfillment.isFulfillmentReviewRequired()) {
            super.handle(event);
        } else {
            fulfillment.finished();
            this.fulfillmentRepository.save(fulfillment);
        }
    }
}
