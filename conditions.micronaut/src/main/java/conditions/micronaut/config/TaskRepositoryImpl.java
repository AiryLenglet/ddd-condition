package conditions.micronaut.config;

import conditions.core.model.task.Task;
import conditions.core.repository.EntityManagerRepository;
import conditions.core.repository.TaskRepository;

import javax.persistence.EntityManager;

public class TaskRepositoryImpl extends EntityManagerRepository<Task> implements TaskRepository {

    public TaskRepositoryImpl(
            EntityManager entityManager
    ) {
        super(entityManager, Task.class);
    }
}
