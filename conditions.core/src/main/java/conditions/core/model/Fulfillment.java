package conditions.core.model;

import conditions.core.event.fulfillment.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import static conditions.common.util.Validate.notNull;

@Entity
public class Fulfillment extends Aggregate {

    @EmbeddedId
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "pooled-lo"
    )
    @GenericGenerator(
            name = "pooled-lo",
            strategy = "conditions.core.model.EmbeddedLongIdSequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "condition_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "3"),
                    @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo")
            })
    private FulfillmentId fulfillmentId;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "CONDITION_ID"))
    })
    private ConditionId conditionId;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Type type;
    private boolean fulfillmentReviewRequired = false;

    Fulfillment() {
        //package-private for hibernate
    }

    private Fulfillment(
            ConditionId conditionId,
            boolean fulfillmentReviewRequired,
            Type type
    ) {
        this.fulfillmentId = new FulfillmentId();
        this.conditionId = notNull(conditionId);
        this.fulfillmentReviewRequired = fulfillmentReviewRequired;
        this.type = notNull(type);
        this.status = Status.OPEN;
    }

    public static Fulfillment createApproval(
            ConditionId conditionId
    ) {
        final var approval = new Fulfillment(
                conditionId,
                false,
                Type.APPROVAL
        );
        approval.addEvent(new ApprovalOpenedEvent(approval));
        return approval;
    }

    public static Fulfillment createFulfillment(
            ConditionId conditionId,
            boolean fulfillmentReviewRequired
    ) {
        final var fulfillment = new Fulfillment(
                conditionId,
                fulfillmentReviewRequired,
                Type.FULFILLMENT
        );
        fulfillment.addEvent(new FulfillmentOpenedEvent(fulfillment));
        return fulfillment;
    }

    public FulfillmentId getFulfillmentId() {
        return fulfillmentId;
    }

    public boolean isFulfillmentReviewRequired() {
        return fulfillmentReviewRequired;
    }

    public ConditionId getConditionId() {
        return conditionId;
    }

    public Status getStatus() {
        return status;
    }

    public Type getType() {
        return type;
    }

    public void cancel() {
        this.status = Status.CANCELLED;
        this.addEvent(new FulfillmentCancelledEvent(this.conditionId, this.fulfillmentId));
    }

    public void finished() {
        this.status = Status.DONE;
        if (this.type == Type.APPROVAL) {
            this.addEvent(new ApprovalFinishedEvent(this.conditionId, this.fulfillmentId));
        } else {
            this.addEvent(new FulfillmentFinishedEvent(this.conditionId, this.fulfillmentId));
        }
    }

    public enum Status {
        OPEN,
        CANCELLED,
        DONE;
    }

    public enum Type {
        APPROVAL,
        FULFILLMENT
    }
}
