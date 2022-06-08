package conditions.core.event.condition;

import conditions.core.event.Event;
import conditions.core.model.Condition;
import conditions.core.model.ConditionId;

public class ConditionCreatedEvent implements Event {
    private final Condition condition;

    public ConditionCreatedEvent(Condition condition) {
        this.condition = condition;
    }

    public ConditionId conditionId() {
        return this.condition.getConditionId();
    }
}