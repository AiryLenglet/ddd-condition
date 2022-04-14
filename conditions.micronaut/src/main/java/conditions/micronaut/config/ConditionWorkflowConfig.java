package conditions.micronaut.config;

import conditions.core.event.DomainEventBus;
import conditions.core.event_handler.ConditionWorkflowConfigurer;
import conditions.core.repository.ConditionRepository;
import conditions.core.repository.FulfillmentRepository;
import conditions.core.repository.TaskRepository;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;

@Bean
public class ConditionWorkflowConfig {

    private final ConditionRepository conditionRepository;
    private final FulfillmentRepository fulfillmentRepository;
    private final TaskRepository taskRepository;
    private final DomainEventBus eventBus;

    public ConditionWorkflowConfig(ConditionRepository conditionRepository, FulfillmentRepository fulfillmentRepository, TaskRepository taskRepository, DomainEventBus eventBus) {
        this.conditionRepository = conditionRepository;
        this.fulfillmentRepository = fulfillmentRepository;
        this.taskRepository = taskRepository;
        this.eventBus = eventBus;
    }

    @EventListener
    public void onStartupEvent(StartupEvent startupEvent) {
        new ConditionWorkflowConfigurer(
                this.conditionRepository,
                this.fulfillmentRepository,
                this.taskRepository,
                this.eventBus
        ).configure();
    }
}
