package conditions.core.event.fulfillment;

import conditions.core.model.ConditionId;
import conditions.core.model.FulfillmentId;

public class FulfillmentVerifiedEvent extends FulfilmentEvent {

    private final boolean reviewRequired;

    public FulfillmentVerifiedEvent(FulfillmentId fulfillmentId, ConditionId conditionId, boolean reviewRequired) {
        super(fulfillmentId, conditionId);
        this.reviewRequired = reviewRequired;
    }

    public boolean isReviewRequired() {
        return reviewRequired;
    }
}
