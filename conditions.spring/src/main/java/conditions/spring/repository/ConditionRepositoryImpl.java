package conditions.spring.repository;

import conditions.core.event.EventBus;
import conditions.core.model.Condition;
import conditions.core.repository.ConditionRepository;
import conditions.core.repository.EventPublisherEntityManagerRepository;

import javax.persistence.EntityManager;

public class ConditionRepositoryImpl extends EventPublisherEntityManagerRepository<Condition> implements ConditionRepository {

    public ConditionRepositoryImpl(
            EventBus eventBus,
            EntityManager entityManager
    ) {
        super(entityManager, Condition.class, eventBus);
    }
}
