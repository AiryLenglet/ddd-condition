package conditions.spring;

import conditions.core.event.EventBus;
import conditions.core.event.approval.AskedChangeEvent;
import conditions.core.event.approval.ConditionAcceptedEvent;
import conditions.core.event.condition.ConditionOpenedEvent;
import conditions.core.event.fulfillment.FulfillmentOpenedEvent;
import conditions.core.event_handler.AskedChangeEventHandler;
import conditions.core.event_handler.ConditionAcceptedEventHandler;
import conditions.core.event_handler.ConditionOpenedEventHandler;
import conditions.core.event_handler.FulfillmentOpenedEventHandler;
import conditions.core.repository.ConditionRepository;
import conditions.core.repository.FulfillmentRepository;
import conditions.core.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
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

        /* APPROVAL */
        this.eventBus.subscribe(ConditionAcceptedEvent.class, new ConditionAcceptedEventHandler(conditionRepository));
        this.eventBus.subscribe(ConditionOpenedEvent.class, new ConditionOpenedEventHandler(conditionRepository, fulfillmentRepository));
        this.eventBus.subscribe(AskedChangeEvent.class, new AskedChangeEventHandler(conditionRepository, this.taskRepository));

        /* FULFILLMENT */
        this.eventBus.subscribe(FulfillmentOpenedEvent.class, new FulfillmentOpenedEventHandler(conditionRepository, this.taskRepository));
    }
}
