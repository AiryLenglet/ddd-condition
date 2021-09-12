package conditions.spring.repository;

import conditions.core.model.draft.EscalationStep;
import conditions.core.model.draft.EscalationStepId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringEscalationStepRepository extends JpaRepository<EscalationStep, EscalationStepId> {
}
