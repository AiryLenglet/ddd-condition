package conditions.core.repository;

import conditions.core.event.Event;
import conditions.core.event.EventBus;
import conditions.core.model.Aggregate;

import javax.persistence.EntityManager;

public class EventPublisherEntityManagerRepository<T> extends EntityManagerRepository<T> {

    private final EventBus eventBus;

    public EventPublisherEntityManagerRepository(
            EntityManager entityManager,
            Class<T> type,
            EventBus eventBus
    ) {
        super(entityManager, type);
        this.eventBus = eventBus;
    }

    @Override
    public void save(T entity) {
        if (entity instanceof Aggregate aggregate) {
            Event event;
            //handling before or after saving ?
            while ((event = aggregate.pollEvent()) != null) {
                this.eventBus.publish(event);
            }
        }
        super.save(entity);
    }

}
