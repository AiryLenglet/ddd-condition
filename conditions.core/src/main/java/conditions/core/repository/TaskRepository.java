package conditions.core.repository;

import conditions.core.model.FulfillmentId;
import conditions.core.model.Task;
import conditions.core.model.TaskId;

import java.util.stream.Stream;

public interface TaskRepository {
    void save(Task task);

    Task findById(TaskId id);

    Stream<Task> findAll(Specification<Task> specification);

    final class Specifications {

        private Specifications() {
        }

        public static Specification<Task> isOpen() {
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), Task.Status.OPEN);
        }

        public static Specification<Task> fulfillmentId(FulfillmentId fulfillmentId) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("fulfillmentId"), fulfillmentId);
        }
    }

}
