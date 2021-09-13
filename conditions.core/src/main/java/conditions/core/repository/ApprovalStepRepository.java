package conditions.core.repository;

import conditions.core.model.ConditionId;
import conditions.core.model.draft.ApprovalStep;

public interface ApprovalStepRepository {
    void save(ApprovalStep approvalStep);

    ApprovalStep findByConditionId(ConditionId conditionId);
}
