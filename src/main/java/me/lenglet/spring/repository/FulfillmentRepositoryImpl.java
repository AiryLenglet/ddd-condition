package me.lenglet.spring.repository;

import me.lenglet.core.event.Event;
import me.lenglet.core.event.EventBus;
import me.lenglet.core.model.Fulfillment;
import me.lenglet.core.repository.FulfillmentRepository;
import org.springframework.stereotype.Service;

@Service
public class FulfillmentRepositoryImpl implements FulfillmentRepository {

    private final SpringFulfillmentRepository fulfillmentRepository;
    private final EventBus eventBus;

    public FulfillmentRepositoryImpl(
            SpringFulfillmentRepository fulfillmentRepository,
            EventBus eventBus
    ) {
        this.fulfillmentRepository = fulfillmentRepository;
        this.eventBus = eventBus;
    }

    @Override
    public Fulfillment save(Fulfillment fulfillment) {
        Event event;
        //handling before or after saving ?
        while ((event = fulfillment.pollEvent()) != null) {
            this.eventBus.publish(event);
        }
        return this.fulfillmentRepository.save(fulfillment);
    }
}
