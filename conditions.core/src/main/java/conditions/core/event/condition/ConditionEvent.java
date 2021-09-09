package conditions.core.event.condition;

import conditions.core.event.Event;
import conditions.core.model.ConditionId;

class ConditionEvent implements Event {

    protected final ConditionId conditionId;

    protected ConditionEvent(
            ConditionId conditionId
    ) {
        this.conditionId = conditionId;
    }

    public ConditionId getConditionId() {
        return conditionId;
    }
}
