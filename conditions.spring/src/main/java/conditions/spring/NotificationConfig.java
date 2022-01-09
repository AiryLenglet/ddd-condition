package conditions.spring;

import conditions.core.event.EventBus;
import conditions.core.event.fulfillment.ConditionFulfilledEvent;
import conditions.core.event.fulfillment.FulfillmentOpenedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationConfig implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private EventBus eventBus;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        this.eventBus.subscribeForAfterCommit(FulfillmentOpenedEvent.class, e -> log.info("Notifying owner he has to fulfill"));
        this.eventBus.subscribeForAfterCommit(ConditionFulfilledEvent.class, e -> log.info("Notifying supervisor he has to verify"));
    }
}
