package me.lenglet.spring.repository;

import me.lenglet.core.model.Fulfillment;
import me.lenglet.core.model.FulfillmentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringFulfillmentRepository extends JpaRepository<Fulfillment, FulfillmentId> {
}
