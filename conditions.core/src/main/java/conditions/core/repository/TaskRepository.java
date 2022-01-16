package conditions.core.repository;

import conditions.core.model.Task;
import conditions.core.model.TaskId;

public interface TaskRepository {
    void save(Task task);

    Task findById(TaskId id);

    Iterable<Task> findAll(Specification<Task> specification);

    final class Specifications {

        private Specifications() {
        }

        public static Specification<Task> isOpen() {
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), Task.Status.OPEN);
        }
    }

}
