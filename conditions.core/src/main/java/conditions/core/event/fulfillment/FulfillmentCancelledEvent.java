package conditions.core.event.fulfillment;

import conditions.core.model.ConditionId;
import conditions.core.model.FulfillmentId;

public class FulfillmentCancelledEvent extends FulfilmentEvent {

    public FulfillmentCancelledEvent(FulfillmentId fulfillmentId, ConditionId conditionId) {
        super(fulfillmentId, conditionId);
    }
}
