package conditions.spring.repository;

import conditions.core.event.EventBus;
import conditions.core.model.Task;
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
