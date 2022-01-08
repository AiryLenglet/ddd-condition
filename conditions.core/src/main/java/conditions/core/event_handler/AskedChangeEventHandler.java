package conditions.core.event_handler;

import conditions.core.event.EventBus;
import conditions.core.event.approval.AskedChangeEvent;
import conditions.core.model.ConditionSetupTask;
import conditions.core.repository.ConditionRepository;
import conditions.core.repository.TaskRepository;

public class AskedChangeEventHandler implements EventBus.Handler<AskedChangeEvent> {

    private final ConditionRepository conditionRepository;
    private final TaskRepository taskRepository;

    public AskedChangeEventHandler(
            ConditionRepository conditionRepository,
            TaskRepository taskRepository
    ) {
        this.conditionRepository = conditionRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public void handle(AskedChangeEvent event) {
        final var condition = this.conditionRepository.findById(event.conditionId());
        this.taskRepository.save(new ConditionSetupTask(
                event.conditionId(),
                event.fulfillmentId(),
                condition.getOwner().getPid(),
                event.taskId()
        ));
    }
}
