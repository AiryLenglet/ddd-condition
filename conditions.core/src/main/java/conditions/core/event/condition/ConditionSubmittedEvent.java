package conditions.core.event.condition;

import conditions.core.model.ConditionId;

public class ConditionSubmittedEvent extends ConditionEvent {
    public ConditionSubmittedEvent(ConditionId conditionId) {
        super(conditionId);
    }
}
