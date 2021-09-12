package conditions.core.event.approval;

import conditions.core.model.ConditionId;
import conditions.core.model.draft.ApprovalStepId;

public class AskedChangeEvent extends ApprovalEvent {
    public AskedChangeEvent(ConditionId conditionId, ApprovalStepId approvalStepId) {
        super(conditionId, approvalStepId);
    }
}
