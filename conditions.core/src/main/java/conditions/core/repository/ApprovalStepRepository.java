package conditions.core.repository;

import conditions.core.model.ConditionId;
import conditions.core.model.draft.ApprovalStep;
import conditions.core.model.draft.ApprovalStepId;

public interface ApprovalStepRepository {
    void save(ApprovalStep approvalStep);

    ApprovalStep findById(ApprovalStepId approvalStepId);

    ApprovalStep findByConditionId(ConditionId conditionId);
}
