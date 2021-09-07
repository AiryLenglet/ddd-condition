package me.lenglet.spring.event_bus;

import me.lenglet.core.event.EventBus;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class EventBusConfig {

    @Bean
    @Scope("singleton")
    public EventBus eventBus(ApplicationEventPublisher applicationEventPublisher) {
        return new InMemoryEventBus(applicationEventPublisher);
    }
}
