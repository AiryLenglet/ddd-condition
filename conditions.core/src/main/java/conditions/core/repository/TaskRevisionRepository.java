package conditions.core.repository;

import conditions.core.model.TaskRevision;

import java.util.stream.Stream;

public interface TaskRevisionRepository {

    void save(TaskRevision taskRevision);

    Stream<TaskRevision> findAll(Specification<TaskRevision> specification);

    final class Specifications {

        public static Specification<TaskRevision> all() {
            return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("id"));
        }

    }
}
