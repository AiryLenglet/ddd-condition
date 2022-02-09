package conditions.core.repository;

import conditions.core.model.ConditionId;
import conditions.core.model.Fulfillment;
import conditions.core.model.FulfillmentId;

import java.util.stream.Stream;

public interface FulfillmentRepository {
    void save(Fulfillment fulfillment);

    Fulfillment findOne(Specification<Fulfillment> fulfillmentId);

    Stream<Fulfillment> findAll(Specification<Fulfillment> specification);

    final class Specifications {

        public static Specification<Fulfillment> id(FulfillmentId fulfillmentId) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), fulfillmentId);
        }

        public static Specification<Fulfillment> conditionId(ConditionId conditionId) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("conditionId"), conditionId);
        }

    }
}
