package me.lenglet.core.event.fulfillment;

import me.lenglet.core.model.ConditionId;
import me.lenglet.core.model.FulfillmentId;

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
