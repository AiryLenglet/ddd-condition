package me.lenglet.core.event_handler;

import lombok.extern.slf4j.Slf4j;
import me.lenglet.core.event.Event;
import me.lenglet.core.event.EventBus;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Slf4j
public class OutsideTransactionHandler<T extends Event> implements EventBus.Handler<T> {

    private final EventBus.Handler<T> delegate;
    EntityManager entityManager;

    public OutsideTransactionHandler(
            EventBus.Handler<T> delegate
    ) {
        this.delegate = delegate;
    }

    @Override
    @Transactional(dontRollbackOn = Exception.class)
    public void handle(T event) {
        try {
            this.delegate.handle(event);
        } catch (Exception e) {
            log.info("Something happened", e);
        }
    }
}
