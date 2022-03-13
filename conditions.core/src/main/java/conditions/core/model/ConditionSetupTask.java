package conditions.core.model;

import conditions.core.event.condition.ConditionSubmittedEvent;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

//@Audited
@Entity
@DiscriminatorValue(value = "CONDITION_SETUP")
public class ConditionSetupTask extends Task {

    public ConditionSetupTask(
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

    public ConditionSetupTask(
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

    @Override
    public void submit() {
        this.addEvent(new ConditionSubmittedEvent(this.conditionId, this.taskId));
        super.submit();
    }

    ConditionSetupTask() {
        super();
    }

}
