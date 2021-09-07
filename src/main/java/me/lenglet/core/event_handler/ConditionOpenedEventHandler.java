package me.lenglet.core.event_handler;

import me.lenglet.core.event.EventBus;
import me.lenglet.core.event.condition.ConditionOpenedEvent;
import me.lenglet.core.repository.ConditionRepository;
import me.lenglet.core.repository.FulfillmentRepository;

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
        final var condition = this.conditionRepository.findById(event.getConditionId());
        this.fulfillmentRepository.save(condition.startNewFulfillment());
    }
}
