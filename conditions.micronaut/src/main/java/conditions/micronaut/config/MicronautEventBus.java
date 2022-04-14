package conditions.micronaut.config;

import conditions.core.event.DomainEventBus;
import conditions.core.event.Event;
import conditions.core.event.EventBus;
import conditions.core.event.IntegrationEventBus;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.transaction.annotation.TransactionalEventListener;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

@Bean
@Singleton
public class MicronautEventBus implements EventBus {

    private static final Logger LOGGER = LoggerFactory.getLogger(MicronautEventBus.class);

    private final ApplicationEventPublisher applicationEventPublisher;
    private final DomainEventBus domainEventBus;
    private final IntegrationEventBus integrationEventBus;

    public MicronautEventBus(
            ApplicationEventPublisher applicationEventPublisher,
            DomainEventBus domainEventBus,
            IntegrationEventBus integrationEventBus
    ) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.domainEventBus = domainEventBus;
        this.integrationEventBus = integrationEventBus;
    }

    @SuppressWarnings("all")
    @Override
    public void publish(Event event) {
        LOGGER.info("Event '{}' published", event);
        this.domainEventBus.publish(event);
        this.applicationEventPublisher.publishEvent(new AfterCommit(event, integrationEventBus::publish));
    }

    @TransactionalEventListener
    void handleAfterCommit(AfterCommit afterCommit) {
        try {
            afterCommit.eventConsumer.accept(afterCommit.event);
        } catch (Exception e) {
            LOGGER.error("Something happened", e);
        }
    }

    private record AfterCommit(
            Event event,
            Consumer<Event> eventConsumer
    ) {
    }
}
