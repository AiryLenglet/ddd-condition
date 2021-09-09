package conditions.core.event.fulfillment;

import conditions.core.model.ConditionId;
import conditions.core.model.FulfillmentId;

public class FulfillmentFinishedEvent extends FulfilmentEvent {

    public FulfillmentFinishedEvent(FulfillmentId fulfillmentId, ConditionId conditionId) {
        super(fulfillmentId, conditionId);
    }
}
