package conditions.spring.event_bus;

import conditions.core.event.EventBus;
import conditions.core.event.approval.ConditionAcceptedEvent;
import conditions.core.event.approval.ConditionRejectedEvent;
import conditions.core.event.condition.ConditionOpenedEvent;
import conditions.core.event.condition.ConditionSubmittedEvent;
import conditions.core.event.fulfillment.ConditionFulfilledEvent;
import conditions.core.event.fulfillment.FulfillmentOpenedEvent;
import conditions.core.event_handler.ConditionAcceptedEventHandler;
import conditions.core.event_handler.ConditionOpenedEventHandler;
import conditions.core.event_handler.ConditionRejectedEventHandler;
import conditions.core.event_handler.ConditionSubmittedEventHandler;
import conditions.core.repository.ApprovalStepRepository;
import conditions.core.repository.ConditionRepository;
import conditions.core.repository.EscalationStepRepository;
import conditions.core.repository.FulfillmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventSubscriptionConfig implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private ConditionRepository conditionRepository;
    @Autowired
    private FulfillmentRepository fulfillmentRepository;
    @Autowired
    private EscalationStepRepository escalationStepRepository;
    @Autowired
    private ApprovalStepRepository approvalStepRepository;

    @Autowired
    private EventBus eventBus;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        this.eventBus.subscribeForAfterCommit(ConditionOpenedEvent.class, event -> {
            throw new RuntimeException("cannot notify HRT-API");
        });

        this.eventBus.subscribe(ConditionRejectedEvent.class, new ConditionRejectedEventHandler(this.escalationStepRepository));
        this.eventBus.subscribe(ConditionSubmittedEvent.class, new ConditionSubmittedEventHandler(approvalStepRepository));
        this.eventBus.subscribe(ConditionAcceptedEvent.class, new ConditionAcceptedEventHandler(conditionRepository));
        this.eventBus.subscribe(ConditionOpenedEvent.class, new ConditionOpenedEventHandler(conditionRepository, fulfillmentRepository));

        //notifications
        this.eventBus.subscribeForAfterCommit(FulfillmentOpenedEvent.class, e -> log.info("Notifying owner he has to fulfill"));
        this.eventBus.subscribeForAfterCommit(ConditionFulfilledEvent.class, e -> log.info("Notifying supervisor he has to verify"));
    }
}
