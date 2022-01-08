package conditions.core.model;

public abstract class NoDecisionTask extends Task<NoDecisionTask.Decision> {

    public NoDecisionTask(
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

    public NoDecisionTask(
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

    NoDecisionTask() {
        super();
    }

    public enum Decision {
    }
}
