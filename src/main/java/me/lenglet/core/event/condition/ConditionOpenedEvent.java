package me.lenglet.core.event.condition;

import me.lenglet.core.model.ConditionId;

public class ConditionOpenedEvent extends ConditionEvent {

    public ConditionOpenedEvent(ConditionId conditionId) {
        super(conditionId);
    }
}
