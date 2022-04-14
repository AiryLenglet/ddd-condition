package conditions.core.event;

public interface Handler<T extends Event> {
    void handle(T event);
}
