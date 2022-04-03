package conditions.core.event.condition;

import conditions.core.event.Event;
import conditions.core.model.ConditionId;

public record ConditionCreatedEvent(
        ConditionId conditionId
) implements Event {
}