package conditions.core.event;

import conditions.core.model.FulfillmentId;

public interface FulfillmentEvent extends ConditionEvent {

    FulfillmentId fulfillmentId();
}
