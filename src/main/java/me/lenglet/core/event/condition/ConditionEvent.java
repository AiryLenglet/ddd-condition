package me.lenglet.core.event.condition;

import me.lenglet.core.event.Event;
import me.lenglet.core.model.ConditionId;

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
