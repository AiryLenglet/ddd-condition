package conditions.micronaut.config;

import conditions.core.event.EventBus;
import conditions.core.model.task.Task;
import conditions.core.repository.EventPublisherEntityManagerRepository;
import conditions.core.repository.TaskRepository;

import javax.persistence.EntityManager;

public class TaskRepositoryImpl extends EventPublisherEntityManagerRepository<Task> implements TaskRepository {

    public TaskRepositoryImpl(
            EventBus eventBus,
            EntityManager entityManager
    ) {
        super(entityManager, Task.class, eventBus);
    }
}
