package conditions.core.event.fulfillment;

import conditions.core.event.FulfillmentEvent;
import conditions.core.model.ConditionId;
import conditions.core.model.Fulfillment;
import conditions.core.model.FulfillmentId;

public class FulfillmentOpenedEvent implements FulfillmentEvent {

    private final Fulfillment fulfillment;

    public FulfillmentOpenedEvent(
            Fulfillment fulfillment
    ) {
        this.fulfillment = fulfillment;
    }

    @Override
    public ConditionId conditionId() {
        return this.fulfillment.getConditionId();
    }

    @Override
    public FulfillmentId fulfillmentId() {
        return this.fulfillment.getFulfillmentId();
    }
}
