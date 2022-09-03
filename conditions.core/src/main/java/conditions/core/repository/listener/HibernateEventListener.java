package conditions.core.repository.listener;

import conditions.core.event.Event;
import conditions.core.event.EventBus;
import conditions.core.model.Aggregate;
import org.hibernate.action.spi.BeforeTransactionCompletionProcess;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;

public class HibernateEventListener implements PostInsertEventListener, PostUpdateEventListener {

    private final EventBus eventBus;

    public HibernateEventListener(
            EventBus eventBus
    ) {
        this.eventBus = eventBus;
    }

    @Override
    public void onPostInsert(PostInsertEvent event) {
        this.consumeEventIfAggregate(event.getEntity(), event.getSession());

    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        this.consumeEventIfAggregate(event.getEntity(), event.getSession());
    }

    private void consumeEventIfAggregate(Object entity, EventSource session) {
        if (entity instanceof Aggregate aggregate) {

            Event event;
            //handling before or after saving ?
            while ((event = aggregate.pollEvent()) != null) {
                session.getActionQueue().registerProcess(new HandleEventBeforeTransactionCompletionProcess(
                        event,
                        this.eventBus
                ));
            }
        }
    }

    public static class HandleEventBeforeTransactionCompletionProcess implements BeforeTransactionCompletionProcess {

        private final Event event;
        private final EventBus eventBus;

        public HandleEventBeforeTransactionCompletionProcess(
                Event event,
                EventBus eventBus
        ) {
            this.event = event;
            this.eventBus = eventBus;
        }

        @Override
        public void doBeforeTransactionCompletion(SessionImplementor session) {
            eventBus.publish(event);
            // flushing is required as hibernate might already have flushed
            session.flush();
        }
    }
}
