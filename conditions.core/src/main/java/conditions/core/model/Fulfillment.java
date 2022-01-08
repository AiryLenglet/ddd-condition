package conditions.core.model;

import conditions.core.event.fulfillment.FulfillmentCancelledEvent;
import conditions.core.event.fulfillment.FulfillmentFinishedEvent;
import conditions.core.event.fulfillment.FulfillmentOpenedEvent;

import javax.persistence.*;

@Entity
public class Fulfillment extends Aggregate {

    @EmbeddedId
    private FulfillmentId fulfillmentId;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "CONDITION_ID"))
    })
    private ConditionId conditionId;
    @Enumerated(EnumType.STRING)
    private Status status;
    private boolean fulfillmentReviewRequired = false;

    Fulfillment() {
        //package-private for hibernate
    }

    Fulfillment(
            ConditionId conditionId,
            boolean fulfillmentReviewRequired
    ) {
        this.fulfillmentId = new FulfillmentId();
        this.conditionId = conditionId;
        this.fulfillmentReviewRequired = fulfillmentReviewRequired;
        this.status = Status.OPEN;
        this.addEvent(new FulfillmentOpenedEvent(this.fulfillmentId, this.conditionId));
    }

    public FulfillmentId getFulfillmentId() {
        return fulfillmentId;
    }


    public void cancel() {
        this.status = Status.CANCELLED;
        this.addEvent(new FulfillmentCancelledEvent(this.fulfillmentId, this.conditionId));
    }

    public void finished() {
        this.status = Status.DONE;
        this.addEvent(new FulfillmentFinishedEvent(this.fulfillmentId, this.conditionId));
    }

    enum Status {
        OPEN,
        CANCELLED,
        DONE;
    }
}
