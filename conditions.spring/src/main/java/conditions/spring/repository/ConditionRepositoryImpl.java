package conditions.spring.repository;

import conditions.core.event.Event;
import conditions.core.event.EventBus;
import conditions.core.model.Condition;
import conditions.core.model.ConditionId;
import conditions.core.repository.ConditionRepository;
import conditions.core.repository.Specification;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class ConditionRepositoryImpl implements ConditionRepository {

    private final SpringConditionRepository springConditionRepository;
    private final EventBus eventBus;
    private EntityManager entityManager;

    public ConditionRepositoryImpl(
            SpringConditionRepository springConditionRepository,
            EventBus eventBus
    ) {
        this.springConditionRepository = springConditionRepository;
        this.eventBus = eventBus;
    }

    @Override
    public void save(Condition condition) {
        Event event;
        //handling before or after saving ?
        while ((event = condition.pollEvent()) != null) {
            this.eventBus.publish(event);
        }
        this.springConditionRepository.save(condition);
    }

    @Override
    public Condition findById(ConditionId id) {
        return this.springConditionRepository.getById(id);
    }

    @Override
    public Iterable<Condition> findAll(Specification<Condition> specification) {
        final var criteriaBuilder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<Condition> query = criteriaBuilder.createQuery(Condition.class);
        final Root<Condition> root = query.from(Condition.class);

        query.where(specification.toPredicate(root, query, criteriaBuilder));

        return this.entityManager.createQuery(query).getResultList();
    }
}
