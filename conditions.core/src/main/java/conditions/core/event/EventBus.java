package conditions.core.event;

public interface EventBus {

    <T extends Event> void subscribe(Class<T> eventClass, Handler<T> handler);

    <T extends Event> void subscribeForAfterCommit(Class<T> eventClass, Handler<T> handler);

    void publish(Event event);

    interface Handler<T extends Event> {
        void handle(T event);
    }
}
