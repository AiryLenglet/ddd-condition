package conditions.micronaut.config;

import conditions.core.event.EventBus;
import conditions.core.factory.Clock;
import conditions.core.repository.listener.HibernateEventListener;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Inject;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;

import javax.persistence.EntityManagerFactory;

@Factory
public class ListenerDependenciesConfig {

    @Inject
    private Clock clock;
    @Inject
    private EntityManagerFactory entityManagerFactory;

    @Inject
    private EventBus eventBus;

    @EventListener
    public void onStartupEvent(StartupEvent startupEvent) {
        final var sessionFactory = this.entityManagerFactory.unwrap(SessionFactoryImpl.class);
        final var registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        final var eventLister = new HibernateEventListener(this.eventBus);
        registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(eventLister);
        registry.getEventListenerGroup(EventType.POST_UPDATE).appendListener(eventLister);
    }

}
