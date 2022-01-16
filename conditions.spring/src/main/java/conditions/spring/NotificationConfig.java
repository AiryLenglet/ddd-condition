package conditions.spring;

import conditions.core.event.EventBus;
import conditions.core.event.fulfillment.ConditionFulfilledEvent;
import conditions.core.event.fulfillment.FulfillmentOpenedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConfig implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger("Notification");

    @Autowired
    private EventBus eventBus;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        this.eventBus.subscribeForAfterCommit(FulfillmentOpenedEvent.class, e -> LOGGER.info("Notifying owner he has to fulfill"));
        this.eventBus.subscribeForAfterCommit(ConditionFulfilledEvent.class, e -> LOGGER.info("Notifying supervisor he has to verify"));
    }
}
