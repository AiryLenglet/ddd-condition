package conditions.core.model;

import conditions.core.event.fulfillment.*;

import javax.persistence.*;

@Entity
public class Fulfillment extends Aggregate {

    @EmbeddedId
    private FulfillmentId fulfillmentId = new FulfillmentId();
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "CONDITION_ID"))
    })
    private ConditionId conditionId;
    @Enumerated(EnumType.STRING)
    private Status status = Status.FULFILLMENT;
    private boolean fulfillmentReviewRequired = false;

    private String fulfillment;
    private String fulfillmentVerificationComment;

    @Enumerated(EnumType.STRING)
    private Fulfillment.FulfillmentVerification fulfillmentVerification;
    private String fulfillmentReviewComment;

    @Enumerated(EnumType.STRING)
    private Fulfillment.FulfillmentReview fulfillmentReview;

    Fulfillment() {
        //package-private for hibernate
    }

    Fulfillment(ConditionId conditionId, boolean fulfillmentReviewRequired) {
        this.conditionId = conditionId;
        this.fulfillmentReviewRequired = fulfillmentReviewRequired;
        this.addEvent(new FulfillmentOpenedEvent(this.fulfillmentId, this.conditionId));
    }

    public FulfillmentId getFulfillmentId() {
        return fulfillmentId;
    }

    public void fulfill(String comment) {
        if (this.status != Status.FULFILLMENT) {
            throw new IllegalArgumentException();
        }
        this.status = Status.VERIFICATION;
        this.addEvent(new ConditionFulfilledEvent());
    }

    public void verify(Fulfillment.FulfillmentVerification fulfillmentVerification, String comment) {
        if (this.status != Status.VERIFICATION) {
            throw new IllegalArgumentException();
        }

        if (fulfillmentVerification == Fulfillment.FulfillmentVerification.CHANGE_REQUEST && comment == null) {
            throw new IllegalArgumentException();
        }

        this.fulfillmentVerificationComment = comment;
        this.fulfillmentVerification = fulfillmentVerification;

        if (fulfillmentVerification == Fulfillment.FulfillmentVerification.CHANGE_REQUEST) {
            this.status = Status.FULFILLMENT;
            this.addEvent(new FulfillmentVerificationAskedForChange());
        } else if (fulfillmentVerification == Fulfillment.FulfillmentVerification.APPROVE) {
            if (this.fulfillmentReviewRequired) {
                this.status = Status.REVIEW;
                //this.addEvent();
            } else {
                this.finished();
            }
        }
    }

    public void review(Fulfillment.FulfillmentReview fulfillmentReview, String comment) {
        if (this.status != Status.REVIEW) {
            throw new IllegalArgumentException();
        }

        if (fulfillmentReview == Fulfillment.FulfillmentReview.CHANGE_REQUEST && comment == null) {
            throw new IllegalArgumentException();
        }

        this.fulfillmentReviewComment = comment;
        this.fulfillmentReview = fulfillmentReview;

        if (fulfillmentReview == Fulfillment.FulfillmentReview.CHANGE_REQUEST) {
            this.status = Status.FULFILLMENT;
            this.addEvent(new FulfillmentReviewAskedForChange());
        } else if (fulfillmentReview == Fulfillment.FulfillmentReview.APPROVE) {
            this.finished();
        }
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
        FULFILLMENT,
        VERIFICATION,
        REVIEW,
        CANCELLED,
        DONE;
    }

    public enum FulfillmentVerification {
        APPROVE,
        CHANGE_REQUEST
    }

    public enum FulfillmentReview {
        APPROVE,
        CHANGE_REQUEST
    }
}
