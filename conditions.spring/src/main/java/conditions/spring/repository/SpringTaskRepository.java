package conditions.spring.repository;

import conditions.core.model.Task;
import conditions.core.model.TaskId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringTaskRepository extends JpaRepository<Task<?>, TaskId> {
}
