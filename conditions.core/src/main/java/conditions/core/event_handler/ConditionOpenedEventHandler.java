package conditions.core.event_handler;

import conditions.core.event.EventBus;
import conditions.core.event.condition.ConditionOpenedEvent;
import conditions.core.repository.ConditionRepository;
import conditions.core.repository.FulfillmentRepository;

import static conditions.core.repository.ConditionRepository.Specifications.conditionId;

public class ConditionOpenedEventHandler implements EventBus.Handler<ConditionOpenedEvent> {

    private final ConditionRepository conditionRepository;
    private final FulfillmentRepository fulfillmentRepository;

    public ConditionOpenedEventHandler(
            ConditionRepository conditionRepository,
            FulfillmentRepository fulfillmentRepository
    ) {
        this.conditionRepository = conditionRepository;
        this.fulfillmentRepository = fulfillmentRepository;
    }

    @Override
    public void handle(ConditionOpenedEvent event) {
        final var condition = this.conditionRepository.findOne(conditionId(event.getConditionId()));
        this.fulfillmentRepository.save(condition.startNewFulfillment());
    }
}
