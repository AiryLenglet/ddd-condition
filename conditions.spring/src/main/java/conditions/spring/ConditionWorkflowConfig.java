package conditions.spring;

import conditions.core.event.EventBus;
import conditions.core.event_handler.ConditionWorkflowConfigurer;
import conditions.core.repository.ConditionRepository;
import conditions.core.repository.FulfillmentRepository;
import conditions.core.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConditionWorkflowConfig implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private ConditionRepository conditionRepository;
    @Autowired
    private FulfillmentRepository fulfillmentRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private EventBus eventBus;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        new ConditionWorkflowConfigurer(
                this.conditionRepository,
                this.fulfillmentRepository,
                this.taskRepository,
                this.eventBus
        ).configure();
    }
}
