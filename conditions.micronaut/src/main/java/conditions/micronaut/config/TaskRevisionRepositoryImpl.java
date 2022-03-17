package conditions.micronaut.config;

import conditions.core.model.TaskRevision;
import conditions.core.repository.EntityManagerRepository;
import conditions.core.repository.TaskRevisionRepository;

import javax.persistence.EntityManager;

public class TaskRevisionRepositoryImpl extends EntityManagerRepository<TaskRevision> implements TaskRevisionRepository {

    public TaskRevisionRepositoryImpl(
            EntityManager entityManager
    ) {
        super(entityManager, TaskRevision.class);
    }
}
