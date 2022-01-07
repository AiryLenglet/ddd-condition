package conditions.core.event.fulfillment;

import conditions.core.event.Event;
import conditions.core.model.ConditionId;
import conditions.core.model.TaskId;

public class ConditionFulfilledEvent implements Event {

    private final ConditionId conditionId;
    private final TaskId taskId;

    public ConditionFulfilledEvent(
            ConditionId conditionId,
            TaskId taskId
    ) {
        this.conditionId = conditionId;
        this.taskId = taskId;
    }
}
