package conditions.core.event.approval;

import conditions.core.event.Event;
import conditions.core.model.ConditionId;
import conditions.core.model.draft.ApprovalStepId;

class ApprovalEvent implements Event {

    protected final ConditionId conditionId;
    protected final ApprovalStepId approvalStepId;

    protected ApprovalEvent(
            ConditionId conditionId,
            ApprovalStepId approvalStepId
    ) {
        this.conditionId = conditionId;
        this.approvalStepId = approvalStepId;
    }

    public ConditionId getConditionId() {
        return conditionId;
    }

    public ApprovalStepId getApprovalStepId() {
        return approvalStepId;
    }
}
