package conditions.core.model.task;

import conditions.common.util.Validate;
import conditions.core.event.approval.ChangeReviewAcceptedEvent;
import conditions.core.event.approval.ChangeReviewDiscardedEvent;
import conditions.core.event.approval.ChangeReviewEscalatedEvent;
import conditions.core.model.ConditionId;
import conditions.core.model.FulfillmentId;
import conditions.core.model.Pid;
import conditions.core.model.TaskId;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "CHANGE_REVIEW")
public class ChangeReviewTask extends DecisionTask<ChangeReviewTask.Decision> {

    public ChangeReviewTask(
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

    ChangeReviewTask() {
        super();
    }

    @Override
    public void submit() {
        Validate.notNull(this.outcome, () -> new IllegalStateException("provide decision"));
        if (this.outcome != Decision.ACCEPT && this.comment == null) {
            throw new IllegalArgumentException("provide comment");
        }

        this.addEvent(switch (this.outcome) {
            case ACCEPT -> new ChangeReviewAcceptedEvent(this.conditionId, this.fulfillmentId, this.taskId);
            case ESCALATE -> new ChangeReviewEscalatedEvent(this.conditionId, this.fulfillmentId, this.taskId);
            case DISCARD -> new ChangeReviewDiscardedEvent(this.conditionId, this.fulfillmentId, this.taskId);
        });

        super.submit();
    }

    public enum Decision {
        ACCEPT,
        ESCALATE,
        DISCARD
    }
}
