package conditions.core.event_handler;

import conditions.core.event.EventBus;
import conditions.core.event.fulfillment.FulfillmentOpenedEvent;
import conditions.core.model.FulfillmentTask;
import conditions.core.repository.ConditionRepository;
import conditions.core.repository.TaskRepository;

import static conditions.core.repository.ConditionRepository.Specifications.conditionId;

public class FulfillmentOpenedEventHandler implements EventBus.Handler<FulfillmentOpenedEvent> {

    private final ConditionRepository conditionRepository;
    private final TaskRepository taskRepository;

    public FulfillmentOpenedEventHandler(
            ConditionRepository conditionRepository,
            TaskRepository taskRepository
    ) {
        this.conditionRepository = conditionRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public void handle(FulfillmentOpenedEvent event) {
        final var condition = this.conditionRepository.findOne(conditionId(event.getConditionId()));
        this.taskRepository.save(new FulfillmentTask(
                event.getConditionId(),
                event.getFulfillmentId(),
                condition.getImposer().getPid()
        ));
    }
}
