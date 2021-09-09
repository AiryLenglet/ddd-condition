package conditions.spring.repository;

import conditions.core.model.Condition;
import conditions.core.model.ConditionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringConditionRepository extends JpaRepository<Condition, ConditionId> {
}
