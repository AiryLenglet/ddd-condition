package conditions.spring.repository;

import conditions.core.model.ConditionId;
import conditions.core.model.draft.ApprovalStep;
import conditions.core.model.draft.ApprovalStepId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringApprovalStepRepository extends JpaRepository<ApprovalStep, ApprovalStepId> {

    ApprovalStep findByConditionId(ConditionId conditionId);
}
