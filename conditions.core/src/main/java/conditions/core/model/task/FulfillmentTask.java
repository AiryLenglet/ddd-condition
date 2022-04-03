package conditions.core.model.task;

import conditions.core.event.fulfillment.ConditionFulfilledEvent;
import conditions.core.model.ConditionId;
import conditions.core.model.FulfillmentId;
import conditions.core.model.Pid;
import conditions.core.model.TaskId;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "FULFILLMENT")
public class FulfillmentTask extends Task {

    public FulfillmentTask(
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

    public FulfillmentTask(
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

    FulfillmentTask() {
        super();
    }

    @Override
    public void submit() {
        if (this.comment == null) {
            throw new IllegalArgumentException("provide com");
        }
        this.addEvent(new ConditionFulfilledEvent(this.conditionId, this.fulfillmentId, this.taskId));
        super.submit();
    }

}
