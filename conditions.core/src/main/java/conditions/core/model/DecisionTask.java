package conditions.core.model;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public abstract class DecisionTask<E extends Enum<E>> extends Task {

    @Enumerated(EnumType.STRING)
    protected E outcome;

    DecisionTask() {
        //package-private for hibernate
    }

    protected DecisionTask(
            ConditionId conditionId,
            FulfillmentId fulfillmentId,
            Pid assignee
    ) {
        super(
                conditionId,
                fulfillmentId,
                assignee
        );
    }

    protected DecisionTask(
            ConditionId conditionId,
            FulfillmentId fulfillmentId,
            Pid assignee,
            TaskId previousTaskId
    ) {
        super(
                conditionId,
                fulfillmentId,
                assignee,
                previousTaskId
        );
    }

    public E getOutcome() {
        return outcome;
    }
}
