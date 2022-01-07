package conditions.core.model;

import conditions.core.event.fulfillment.ConditionFulfilledEvent;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "FULFILLMENT")
public class FulfillmentTask extends Task<FulfillmentTask.Decision> {

    public FulfillmentTask(
            ConditionId conditionId,
            TaskId previousTaskId
    ) {
        super(conditionId, previousTaskId);
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
        super.submit();
    }

    public enum Decision {

    }
}
