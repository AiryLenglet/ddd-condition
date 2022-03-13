package conditions.core.model;

import conditions.common.util.Validate;
import conditions.core.event.approval.AskedChangeEvent;
import conditions.core.event.approval.ConditionAcceptedEvent;
import conditions.core.event.approval.ConditionRejectedEvent;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

//@Audited
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
            case ACCEPT -> new ConditionAcceptedEvent(this.conditionId, this.fulfillmentId, this.taskId);
            case REJECT -> new ConditionRejectedEvent(this.conditionId, this.fulfillmentId, this.taskId);
            case ASK_FOR_CHANGE -> new AskedChangeEvent(this.conditionId, this.fulfillmentId, this.taskId);
        });

        super.submit();
    }

    public enum Decision {
        ACCEPT,
        ASK_FOR_CHANGE,
        REJECT
    }
}
