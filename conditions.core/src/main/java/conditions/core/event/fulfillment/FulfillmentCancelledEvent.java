package conditions.core.event.fulfillment;

import conditions.core.event.FulfillmentEvent;
import conditions.core.model.ConditionId;
import conditions.core.model.FulfillmentId;

public record FulfillmentCancelledEvent(
        ConditionId conditionId,
        FulfillmentId fulfillmentId
) implements FulfillmentEvent {
}
