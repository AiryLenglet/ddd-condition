package conditions.core.model.task;

import conditions.common.util.Validate;
import conditions.core.event.approval.RefusalReviewDiscardedEvent;
import conditions.core.event.approval.RefusalReviewEscalatedEvent;
import conditions.core.model.ConditionId;
import conditions.core.model.FulfillmentId;
import conditions.core.model.Pid;
import conditions.core.model.TaskId;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "REFUSAL_REVIEW")
public class RefusalReviewTask extends DecisionTask<RefusalReviewTask.Decision> {

    public RefusalReviewTask(
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

    RefusalReviewTask() {
        super();
    }

    @Override
    public void submit() {
        Validate.notNull(this.outcome, () -> new IllegalStateException("provide decision"));
        Validate.notNull(this.comment, () -> new IllegalStateException("provide comment"));

        this.addEvent(switch (this.outcome) {
            case ESCALATE -> new RefusalReviewEscalatedEvent(this.conditionId, this.fulfillmentId, this.taskId);
            case DISCARD -> new RefusalReviewDiscardedEvent(this.conditionId, this.fulfillmentId, this.taskId);
        });

        super.submit();
    }

    public enum Decision {
        ESCALATE,
        DISCARD
    }
}
