package conditions.core.model;

import conditions.core.event.fulfillment.ConditionFulfilledEvent;
import org.hibernate.envers.Audited;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Audited
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
        this.addEvent(new ConditionFulfilledEvent(this.conditionId, this.taskId));
    }

}
