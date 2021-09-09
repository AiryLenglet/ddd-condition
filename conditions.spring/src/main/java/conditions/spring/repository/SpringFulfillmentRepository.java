package conditions.spring.repository;

import conditions.core.model.Fulfillment;
import conditions.core.model.FulfillmentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringFulfillmentRepository extends JpaRepository<Fulfillment, FulfillmentId> {
}
