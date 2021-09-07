package me.lenglet.core.event.fulfillment;

import me.lenglet.core.model.ConditionId;
import me.lenglet.core.model.FulfillmentId;

public class FulfillmentCancelledEvent extends FulfilmentEvent {

    public FulfillmentCancelledEvent(FulfillmentId fulfillmentId, ConditionId conditionId) {
        super(fulfillmentId, conditionId);
    }
}
