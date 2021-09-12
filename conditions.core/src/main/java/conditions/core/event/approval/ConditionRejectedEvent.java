package conditions.core.event.approval;

import conditions.core.model.ConditionId;
import conditions.core.model.draft.ApprovalStepId;

public class ConditionRejectedEvent extends ApprovalEvent {

    public ConditionRejectedEvent(ConditionId conditionId, ApprovalStepId approvalStepId) {
        super(conditionId, approvalStepId);
    }
}
