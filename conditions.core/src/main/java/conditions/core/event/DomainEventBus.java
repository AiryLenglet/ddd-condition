package conditions.core.event;

public interface DomainEventBus {

    <T extends Event> void subscribe(Class<T> eventClass, Handler<T> handler);

    void publish(Event event);

}
