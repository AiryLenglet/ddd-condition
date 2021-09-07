package me.lenglet.spring.event_bus;

import lombok.extern.slf4j.Slf4j;
import me.lenglet.core.event.EventBus;
import me.lenglet.core.event.condition.ConditionOpenedEvent;
import me.lenglet.core.event.fulfillment.ConditionFulfilledEvent;
import me.lenglet.core.event.fulfillment.FulfillmentOpenedEvent;
import me.lenglet.core.event.fulfillment.FulfillmentVerifiedEvent;
import me.lenglet.core.event_handler.ConditionOpenedEventHandler;
import me.lenglet.core.event_handler.OutsideTransactionHandler;
import me.lenglet.core.repository.ConditionRepository;
import me.lenglet.core.repository.FulfillmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventSubscriptionConfig implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private ConditionRepository conditionRepository;

    @Autowired
    private FulfillmentRepository fulfillmentRepository;

    @Autowired
    private EventBus eventBus;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        this.eventBus.subscribe(ConditionOpenedEvent.class, new ConditionOpenedEventHandler(conditionRepository, fulfillmentRepository));
        this.eventBus.subscribeForAfterCommit(ConditionOpenedEvent.class, event -> {
            throw new RuntimeException("cannot notify HRT-API");
        });

        //notifications
        this.eventBus.subscribe(FulfillmentOpenedEvent.class, e -> log.info("Notifying owner he has to fulfill"));
        this.eventBus.subscribe(ConditionFulfilledEvent.class, e -> log.info("Notifying supervisor he has to verify"));
        this.eventBus.subscribe(FulfillmentVerifiedEvent.class, new OutsideTransactionHandler<>(e -> {
            if (e.isReviewRequired()) {
                log.info("Notifying supervisor he has to verify");
            }
        }));
    }
}
