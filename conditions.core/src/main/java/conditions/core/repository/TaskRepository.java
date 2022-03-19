package conditions.core.repository;

import conditions.core.model.ConditionId;
import conditions.core.model.FulfillmentId;
import conditions.core.model.Task;
import conditions.core.model.TaskId;

import java.util.stream.Stream;

public interface TaskRepository {
    void save(Task task);

    Task findOne(Specification<Task> specification);

    Stream<Task> findAll(Specification<Task> specification);

    final class Specifications {

        private Specifications() {
        }

        public static Specification<Task> id(TaskId taskId) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("taskId"), taskId);
        }

        public static Specification<Task> id(String taskId) {
            return id(new TaskId(taskId));
        }

        public static Specification<Task> isOpen() {
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), Task.Status.OPEN);
        }

        public static Specification<Task> fulfillmentId(FulfillmentId fulfillmentId) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("fulfillmentId"), fulfillmentId);
        }

        public static Specification<Task> fulfillmentId(String fulfillmentId) {
            return fulfillmentId(new FulfillmentId(fulfillmentId));
        }

        public static Specification<Task> conditionId(ConditionId conditionId) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("conditionId"), conditionId);
        }

        public static Specification<Task> conditionId(String conditionId) {
            return conditionId(new ConditionId(conditionId));
        }
    }

}
