package conditions.core.event.condition;

import conditions.core.event.ConditionEvent;
import conditions.core.model.ConditionId;
import conditions.core.model.Pid;

public record OwnerChangedEvent(
        Pid previousOwner,
        Pid newOwner,
        ConditionId conditionId
) implements ConditionEvent {
}
