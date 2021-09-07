package me.lenglet.core.event.condition;

import me.lenglet.core.event.Event;
import me.lenglet.core.model.ConditionId;

public class ConditionClosedEvent implements Event {

    private final ConditionId conditionId;

    public ConditionClosedEvent(ConditionId id) {
        this.conditionId = id;
    }
}
