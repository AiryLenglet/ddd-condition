package conditions.micronaut.config;

import conditions.core.event.*;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;

@Factory
public class EventBusFactory {

    @Singleton
    public DomainEventBus domainEventBus() {
        return new InMemoryDomainEventBus();
    }

    @Singleton
    public IntegrationEventBus integrationEventBus() {
        return new IntegrationEventBus() {
            @Override
            public <T extends Event> void subscribe(Class<T> eventClass, Handler<T> handler) {

            }

            @Override
            public void publish(Event event) {

            }
        };
    }
}
