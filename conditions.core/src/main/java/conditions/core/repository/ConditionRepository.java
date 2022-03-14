package conditions.core.repository;

import conditions.core.model.Condition;
import conditions.core.model.ConditionId;

import java.util.stream.Stream;

public interface ConditionRepository {
    void save(Condition condition);

    Condition findOne(Specification<Condition> specification);

    Stream<Condition> findAll(Specification<Condition> specification);

    final class Specifications {

        public static Specification<Condition> conditionId(ConditionId conditionId) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("conditionId"), conditionId);
        }

        public static Specification<Condition> all() {
            return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("conditionId"));
        }

    }
}
