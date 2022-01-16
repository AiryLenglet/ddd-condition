package conditions.spring.repository;

import conditions.core.model.Task;
import conditions.core.model.TaskId;
import org.springframework.data.repository.history.RevisionRepository;

public interface EnversTaskRevRepository extends RevisionRepository<Task, TaskId, Long> {
}
