package conditions.core.repository;

import conditions.core.model.Condition;
import conditions.core.model.ConditionId;

public interface ConditionRepository {
    void save(Condition condition);

    Condition findById(ConditionId id);
}
