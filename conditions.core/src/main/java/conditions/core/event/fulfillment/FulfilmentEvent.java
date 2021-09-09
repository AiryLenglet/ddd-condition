package conditions.core.event.fulfillment;

import conditions.core.event.Event;
import conditions.core.model.ConditionId;
import conditions.core.model.FulfillmentId;

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
