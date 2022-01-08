package conditions.core.event.condition;

import conditions.core.event.Event;
import conditions.core.model.ConditionId;
import conditions.core.model.TaskId;

public record ConditionSubmittedEvent(
        ConditionId conditionId,
        TaskId taskId
) implements Event {
}