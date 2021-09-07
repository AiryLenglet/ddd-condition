package me.lenglet.spring.event_bus;

import lombok.extern.slf4j.Slf4j;
import me.lenglet.core.event.Event;
import me.lenglet.core.event.EventBus;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public class InMemoryEventBus implements EventBus {

    final Map<Class<?>, Collection<Handler<?>>> handlers = new ConcurrentHashMap<>();
    final Map<Class<?>, Collection<Handler<?>>> handlers_ = new ConcurrentHashMap<>();

    private ApplicationEventPublisher applicationEventPublisher;

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
        this.handlers_.computeIfAbsent(eventClass, c -> new ConcurrentLinkedQueue<>())
                .add(handler);
    }

    @SuppressWarnings("all")
    @Override
    public void publish(Event event) {
        log.info("Event '{}' published", event);
        final var handlers = this.handlers.get(event.getClass());
        if (handlers != null) {
            handlers.stream()
                    .map(handler -> (Handler) handler)
                    .peek(handler -> log.info("Event handled by '{}'", handler.getClass()))
                    .forEach(handler -> handler.handle(event));
        }

        final var handlers_ = this.handlers_.get(event.getClass());
        if (handlers_ != null) {
            handlers_.stream()
                    .map(handler -> (Handler) handler)
                    .peek(handler -> log.info("Event handled by '{}'", handler.getClass()))
                    .map(handler -> new AfterCommit(event, handler))
                    .forEach(e -> this.applicationEventPublisher.publishEvent(e));
        }
    }

    public static class AfterCommit {

        private final Event event;
        private final Handler<Event> handler;

        public AfterCommit(
                Event event,
                Handler<Event> handler
        ) {
            this.event = event;
            this.handler = handler;
        }
    }

    @TransactionalEventListener
    void handleAfterCommit(AfterCommit afterCommit) {
        log.info("Handling after commit");
        try {
            afterCommit.handler.handle(afterCommit.event);
        } catch (Exception e) {
            log.error("Something happened", e);
        }
    }
}
