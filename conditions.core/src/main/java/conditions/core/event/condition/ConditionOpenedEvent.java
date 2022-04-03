package conditions.core.event.condition;

import conditions.core.event.ConditionEvent;
import conditions.core.model.ConditionId;

public record ConditionOpenedEvent(
        ConditionId conditionId
) implements ConditionEvent {
}
