package conditions.core.event_handler;

import conditions.core.event.EventBus;
import conditions.core.event.approval.AskedChangeEvent;
import conditions.core.repository.ConditionRepository;
import conditions.core.repository.FulfillmentRepository;

public class AskedChangeEventHandler implements EventBus.Handler<AskedChangeEvent> {

    private final ConditionRepository conditionRepository;
    private final FulfillmentRepository fulfillmentRepository;

    public AskedChangeEventHandler(
            ConditionRepository conditionRepository,
            FulfillmentRepository fulfillmentRepository
    ) {
        this.conditionRepository = conditionRepository;
        this.fulfillmentRepository = fulfillmentRepository;
    }

    @Override
    public void handle(AskedChangeEvent event) {

    }
}
