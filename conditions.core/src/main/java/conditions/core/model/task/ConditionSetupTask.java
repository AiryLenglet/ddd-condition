package conditions.core.model.task;

import conditions.core.event.condition.ConditionSetupedEvent;
import conditions.core.model.ConditionId;
import conditions.core.model.FulfillmentId;
import conditions.core.model.Pid;
import conditions.core.model.TaskId;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

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
        this.addEvent(new ConditionSetupedEvent(this.conditionId, this.fulfillmentId, this.taskId));
        super.submit();
    }

    ConditionSetupTask() {
        super();
    }

}
