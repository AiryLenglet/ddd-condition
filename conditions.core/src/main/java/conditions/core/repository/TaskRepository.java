package conditions.core.repository;

import conditions.core.model.Task;
import conditions.core.model.TaskId;

public interface TaskRepository {
    void save(Task<?> task);

    Task<?> findById(TaskId id);

}
