package conditions.core.event.fulfillment;

import conditions.core.model.ConditionId;
import conditions.core.model.FulfillmentId;

public class FulfillmentOpenedEvent extends FulfilmentEvent {

    public FulfillmentOpenedEvent(FulfillmentId fulfillmentId, ConditionId conditionId) {
        super(fulfillmentId, conditionId);
    }
}
