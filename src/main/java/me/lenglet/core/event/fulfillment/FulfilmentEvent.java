package me.lenglet.core.event.fulfillment;

import me.lenglet.core.event.Event;
import me.lenglet.core.model.ConditionId;
import me.lenglet.core.model.FulfillmentId;

abstract class FulfilmentEvent implements Event {

    protected final FulfillmentId fulfillmentId;
    protected final ConditionId conditionId;

    FulfilmentEvent(
            FulfillmentId fulfillmentId,
            ConditionId conditionId
    ) {
        this.fulfillmentId = fulfillmentId;
        this.conditionId = conditionId;
    }

    public FulfillmentId getFulfillmentId() {
        return fulfillmentId;
    }

    public ConditionId getConditionId() {
        return conditionId;
    }
}
