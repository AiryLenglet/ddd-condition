package conditions.core.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class InMemoryDomainEventBus implements DomainEventBus {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryDomainEventBus.class);

    private final Map<Class<?>, Collection<Handler<?>>> handlers = new ConcurrentHashMap<>();

    @Override
    public <T extends Event> void subscribe(Class<T> eventClass, Handler<T> handler) {
        this.handlers.computeIfAbsent(eventClass, c -> new ConcurrentLinkedQueue<>())
                .add(handler);
    }

    @Override
    public void publish(Event event) {
        final var handlers = this.handlers.get(event.getClass());
        if (handlers != null) {
            handlers.stream()
                    .map(handler -> (Handler) handler)
                    .peek(handler -> LOGGER.info("Event handled by '{}'", handler.getClass()))
                    .forEach(handler -> handler.handle(event));
        }
    }
}
