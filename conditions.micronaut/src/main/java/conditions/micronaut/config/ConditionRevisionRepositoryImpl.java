package conditions.micronaut.config;

import conditions.core.model.ConditionRevision;
import conditions.core.repository.ConditionRevisionRepository;
import conditions.core.repository.EntityManagerRepository;

import javax.persistence.EntityManager;

public class ConditionRevisionRepositoryImpl extends EntityManagerRepository<ConditionRevision> implements ConditionRevisionRepository {

    public ConditionRevisionRepositoryImpl(
            EntityManager entityManager
    ) {
        super(entityManager, ConditionRevision.class);
    }
}
