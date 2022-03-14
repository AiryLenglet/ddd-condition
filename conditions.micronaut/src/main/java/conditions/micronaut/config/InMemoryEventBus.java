package conditions.micronaut.config;

import conditions.core.event.Event;
import conditions.core.event.EventBus;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.transaction.annotation.TransactionalEventListener;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Bean
@Singleton
public class InMemoryEventBus implements EventBus {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryEventBus.class);

    private final Map<Class<?>, Collection<Handler<?>>> handlers = new ConcurrentHashMap<>();
    private final Map<Class<?>, Collection<Handler<?>>> postCommitHandlers = new ConcurrentHashMap<>();
    private final ApplicationEventPublisher applicationEventPublisher;

    public InMemoryEventBus(
            ApplicationEventPublisher applicationEventPublisher
    ) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public <T extends Event> void subscribe(Class<T> eventClass, Handler<T> handler) {
        this.handlers.computeIfAbsent(eventClass, c -> new ConcurrentLinkedQueue<>())
                .add(handler);
    }

    @Override
    public <T extends Event> void subscribeForAfterCommit(Class<T> eventClass, Handler<T> handler) {
        this.postCommitHandlers.computeIfAbsent(eventClass, c -> new ConcurrentLinkedQueue<>())
                .add(handler);
    }

    @SuppressWarnings("all")
    @Override
    public void publish(Event event) {
        LOGGER.info("Event '{}' published", event);
        final var handlers = this.handlers.get(event.getClass());
        if (handlers != null) {
            handlers.stream()
                    .map(handler -> (Handler) handler)
                    .peek(handler -> LOGGER.info("Event handled by '{}'", handler.getClass()))
                    .forEach(handler -> handler.handle(event));
        }


        final var handlers_ = this.postCommitHandlers.get(event.getClass());
        if (handlers_ != null) {
            handlers_.stream()
                    .map(handler -> (Handler) handler)
                    .map(handler -> new AfterCommit(event, handler))
                    .forEach(this.applicationEventPublisher::publishEvent);
        }

    }

    @TransactionalEventListener
    void handleAfterCommit(AfterCommit afterCommit) {
        LOGGER.info("Handling post commit event {} with {}", afterCommit.event, afterCommit.handler);
        try {
            afterCommit.handler.handle(afterCommit.event);
        } catch (Exception e) {
            LOGGER.error("Something happened", e);
        }
    }

    private record AfterCommit(
            Event event,
            Handler<Event> handler
    ) {
    }
}
