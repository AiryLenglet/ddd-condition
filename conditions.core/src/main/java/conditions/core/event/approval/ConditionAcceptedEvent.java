package conditions.core.event.approval;

import conditions.core.model.ConditionId;
import conditions.core.model.draft.ApprovalStepId;

public class ConditionAcceptedEvent extends ApprovalEvent {
    public ConditionAcceptedEvent(ConditionId conditionId, ApprovalStepId approvalStepId) {
        super(conditionId, approvalStepId);
    }
}
