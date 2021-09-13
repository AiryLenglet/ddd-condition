package conditions.spring.repository;

import conditions.core.event.Event;
import conditions.core.event.EventBus;
import conditions.core.model.Fulfillment;
import conditions.core.repository.FulfillmentRepository;
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
    public void save(Fulfillment fulfillment) {
        Event event;
        //handling before or after saving ?
        while ((event = fulfillment.pollEvent()) != null) {
            this.eventBus.publish(event);
        }
        this.fulfillmentRepository.save(fulfillment);
    }
}
