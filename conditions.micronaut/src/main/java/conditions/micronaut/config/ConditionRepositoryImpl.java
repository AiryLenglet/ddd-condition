package conditions.micronaut.config;

import conditions.core.event.EventBus;
import conditions.core.model.Condition;
import conditions.core.repository.ConditionRepository;
import conditions.core.repository.EntityManagerRepository;

import javax.persistence.EntityManager;

public class ConditionRepositoryImpl extends EntityManagerRepository<Condition> implements ConditionRepository {

    public ConditionRepositoryImpl(
            EntityManager entityManager
    ) {
        super(entityManager, Condition.class);
    }
}
