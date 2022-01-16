package conditions.core.model;

import conditions.common.util.Validate;
import conditions.core.event.fulfillment.FulfillmentReviewAskedForChangeEvent;
import conditions.core.event.fulfillment.FulfillmentVerifiedEvent;
import org.hibernate.envers.Audited;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Audited
@Entity
@DiscriminatorValue(value = "REVIEW")
public class ReviewTask extends DecisionTask<ReviewTask.Decision> {

    public ReviewTask(
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

    ReviewTask() {
        super();
    }

    @Override
    public void submit() {
        Validate.notNull(this.outcome, () -> new IllegalStateException("provide decision"));
        if (this.outcome == Decision.ASK_FOR_CHANGE && this.comment == null) {
            throw new IllegalArgumentException("provide comment");
        }

        this.addEvent(this.outcome == Decision.ACCEPT ?
                new FulfillmentVerifiedEvent(this.conditionId, this.fulfillmentId, this.taskId) :
                new FulfillmentReviewAskedForChangeEvent(this.conditionId, this.fulfillmentId, this.taskId));

        super.submit();
    }

    public enum Decision {
        ACCEPT,
        ASK_FOR_CHANGE
    }
}
