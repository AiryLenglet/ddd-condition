package conditions.core.repository;

import conditions.core.model.ConditionRevision;

import java.util.stream.Stream;

public interface ConditionRevisionRepository {

    void save(ConditionRevision condition);

    Stream<ConditionRevision> findAll(Specification<ConditionRevision> specification);

    final class Specifications {

        public static Specification<ConditionRevision> all() {
            return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("id"));
        }

    }
}
