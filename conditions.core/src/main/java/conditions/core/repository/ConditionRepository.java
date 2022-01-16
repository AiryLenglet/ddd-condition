package conditions.core.repository;

import conditions.core.model.Condition;
import conditions.core.model.ConditionId;

import java.util.stream.Stream;

public interface ConditionRepository {
    void save(Condition condition);

    Condition findById(ConditionId id);

    Stream<Condition> findAll(Specification<Condition> specification);
}
