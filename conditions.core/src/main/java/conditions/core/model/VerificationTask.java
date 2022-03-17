package conditions.core.model;

import conditions.common.util.Validate;
import conditions.core.event.fulfillment.FulfillmentReviewedEvent;
import conditions.core.event.fulfillment.FulfillmentVerificationAskedForChangeEvent;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "VERIFICATION")
public class VerificationTask extends DecisionTask<VerificationTask.Decision> {

    public VerificationTask(
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

    VerificationTask() {
        super();
    }

    @Override
    public void submit() {
        Validate.notNull(this.outcome, () -> new IllegalStateException("provide decision"));
        if (this.outcome == Decision.ASK_FOR_CHANGE && this.comment == null) {
            throw new IllegalArgumentException("provide comment");
        }

        this.addEvent(this.outcome == Decision.ACCEPT ?
                new FulfillmentReviewedEvent(this.conditionId, this.fulfillmentId, this.taskId) :
                new FulfillmentVerificationAskedForChangeEvent(this.conditionId, this.fulfillmentId, this.taskId));

        super.submit();
    }

    public enum Decision {
        ACCEPT,
        ASK_FOR_CHANGE
    }
}
