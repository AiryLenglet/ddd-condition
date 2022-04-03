package conditions.core.model.task;

import conditions.common.util.Validate;
import conditions.core.event.approval.ApprovalAskedChangeEvent;
import conditions.core.event.approval.ApprovalAcceptedEvent;
import conditions.core.event.approval.ApprovalRejectedEvent;
import conditions.core.model.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "APPROVAL")
public class ApprovalTask extends DecisionTask<ApprovalTask.Decision> {

    public ApprovalTask(
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

    ApprovalTask() {
        super();
    }

    @Override
    public void submit() {
        Validate.notNull(this.outcome, () -> new IllegalStateException("provide decision"));
        if (this.outcome != Decision.ACCEPT && this.comment == null) {
            throw new IllegalArgumentException("provide comment");
        }

        this.addEvent(switch (this.outcome) {
            case ACCEPT -> new ApprovalAcceptedEvent(this.conditionId, this.fulfillmentId, this.taskId);
            case REJECT -> new ApprovalRejectedEvent(this.conditionId, this.fulfillmentId, this.taskId);
            case ASK_FOR_CHANGE -> new ApprovalAskedChangeEvent(this.conditionId, this.fulfillmentId, this.taskId);
        });

        super.submit();
    }

    public enum Decision {
        ACCEPT,
        ASK_FOR_CHANGE,
        REJECT
    }
}
