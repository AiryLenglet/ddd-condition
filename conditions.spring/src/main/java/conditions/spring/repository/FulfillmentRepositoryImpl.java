package conditions.spring.repository;

import conditions.core.event.Event;
import conditions.core.event.EventBus;
import conditions.core.model.Fulfillment;
import conditions.core.model.FulfillmentId;
import conditions.core.repository.FulfillmentRepository;
import conditions.core.repository.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.stream.Stream;

@Service
public class FulfillmentRepositoryImpl implements FulfillmentRepository {

    private final SpringFulfillmentRepository fulfillmentRepository;
    private final EventBus eventBus;
    private final EntityManager entityManager;

    public FulfillmentRepositoryImpl(
            SpringFulfillmentRepository fulfillmentRepository,
            EventBus eventBus,
            EntityManager entityManager
    ) {
        this.fulfillmentRepository = fulfillmentRepository;
        this.eventBus = eventBus;
        this.entityManager = entityManager;
    }

    @Override
    public void save(Fulfillment fulfillment) {
        Event event;
        //handling before or after saving ?
        while ((event = fulfillment.pollEvent()) != null) {
            this.eventBus.publish(event);
        }
        this.fulfillmentRepository.save(fulfillment);
    }

    @Override
    public Fulfillment findById(FulfillmentId fulfillmentId) {
        return this.fulfillmentRepository.getById(fulfillmentId);
    }

    @Override
    public Stream<Fulfillment> findAll(Specification<Fulfillment> specification) {
        final var criteriaBuilder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<Fulfillment> query = criteriaBuilder.createQuery(Fulfillment.class);
        final Root<Fulfillment> root = query.from(Fulfillment.class);

        query.where(specification.toPredicate(root, query, criteriaBuilder));

        return this.entityManager.createQuery(query).getResultList().stream();
    }
}
