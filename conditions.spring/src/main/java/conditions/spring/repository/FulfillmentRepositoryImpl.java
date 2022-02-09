package conditions.spring.repository;

import conditions.core.event.EventBus;
import conditions.core.model.Fulfillment;
import conditions.core.repository.EventPublisherEntityManagerRepository;
import conditions.core.repository.FulfillmentRepository;

import javax.persistence.EntityManager;

public class FulfillmentRepositoryImpl extends EventPublisherEntityManagerRepository<Fulfillment> implements FulfillmentRepository {

    public FulfillmentRepositoryImpl(
            EventBus eventBus,
            EntityManager entityManager
    ) {
        super(entityManager, Fulfillment.class, eventBus);
    }

}
