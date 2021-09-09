package conditions.core.event.condition;

import conditions.core.model.ConditionId;

public class ConditionOpenedEvent extends ConditionEvent {

    public ConditionOpenedEvent(ConditionId conditionId) {
        super(conditionId);
    }
}
