package me.lenglet.core.event.fulfillment;

import me.lenglet.core.model.ConditionId;
import me.lenglet.core.model.FulfillmentId;

public class FulfillmentOpenedEvent extends FulfilmentEvent {

    public FulfillmentOpenedEvent(FulfillmentId fulfillmentId, ConditionId conditionId) {
        super(fulfillmentId, conditionId);
    }
}
