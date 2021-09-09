package conditions.core.event.condition;

import conditions.core.event.Event;
import conditions.core.model.ConditionId;

public class ConditionClosedEvent implements Event {

    private final ConditionId conditionId;

    public ConditionClosedEvent(ConditionId id) {
        this.conditionId = id;
    }
}
