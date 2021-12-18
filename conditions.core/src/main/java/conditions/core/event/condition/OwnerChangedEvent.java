package conditions.core.event.condition;

import conditions.core.model.ConditionId;
import lombok.ToString;

@ToString
public class OwnerChangedEvent extends ConditionEvent {

    private final String previousOwner;
    private final String newOwner;

    public OwnerChangedEvent(
            String previousOwner,
            String newOwner,
            ConditionId conditionId
    ) {
        super(conditionId);
        this.previousOwner = previousOwner;
        this.newOwner = newOwner;
    }

    public String getPreviousOwner() {
        return previousOwner;
    }

    public String getNewOwner() {
        return newOwner;
    }
}
