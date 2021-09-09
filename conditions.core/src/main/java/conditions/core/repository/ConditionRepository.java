package conditions.core.repository;

import conditions.core.model.Condition;
import conditions.core.model.ConditionId;

public interface ConditionRepository {
    Condition save(Condition condition);

    Condition findById(ConditionId id);
}
