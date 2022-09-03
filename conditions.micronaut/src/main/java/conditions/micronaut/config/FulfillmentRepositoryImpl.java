package conditions.micronaut.config;

import conditions.core.model.Fulfillment;
import conditions.core.repository.EntityManagerRepository;
import conditions.core.repository.FulfillmentRepository;

import javax.persistence.EntityManager;

public class FulfillmentRepositoryImpl extends EntityManagerRepository<Fulfillment> implements FulfillmentRepository {

    public FulfillmentRepositoryImpl(
            EntityManager entityManager
    ) {
        super(entityManager, Fulfillment.class);
    }

}
