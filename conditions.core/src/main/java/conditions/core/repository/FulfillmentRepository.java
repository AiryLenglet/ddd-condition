package conditions.core.repository;

import conditions.core.model.Fulfillment;
import conditions.core.model.FulfillmentId;

public interface FulfillmentRepository {
    void save(Fulfillment fulfillment);

    Fulfillment findById(FulfillmentId fulfillmentId);

    Iterable<Fulfillment> findAll(Specification<Fulfillment> specification);
}
