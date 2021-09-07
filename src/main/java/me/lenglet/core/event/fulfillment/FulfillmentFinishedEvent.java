package me.lenglet.core.event.fulfillment;

import me.lenglet.core.model.ConditionId;
import me.lenglet.core.model.FulfillmentId;

public class FulfillmentFinishedEvent extends FulfilmentEvent {

    public FulfillmentFinishedEvent(FulfillmentId fulfillmentId, ConditionId conditionId) {
        super(fulfillmentId, conditionId);
    }
}
