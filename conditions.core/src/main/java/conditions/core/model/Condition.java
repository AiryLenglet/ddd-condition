package conditions.core.model;

import conditions.core.event.condition.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.Objects;

import static conditions.common.util.Validate.notNull;

@Entity
public class Condition extends Aggregate {

    private static final Logger LOGGER = LoggerFactory.getLogger(Condition.class);

    @EmbeddedId
    private ConditionId conditionId = new ConditionId();
    @Enumerated(EnumType.STRING)
    private Status status = Status.DRAFT;
    private String type;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "owner"))
    private Pid owner;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "imposer"))
    private Pid imposer;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "supervisor"))
    private Pid supervisor;
    private boolean fulfillmentReviewRequired = true;
    private boolean isRecurring = false;
    @Embedded
    private Country bookingLocation;
    @Enumerated(EnumType.STRING)
    private Classification classification = Classification.ASSET;
    @Version
    private Long version;
    @Embedded
    private Metadata metadata = new Metadata();

    Condition() {
        //package-private for hibernate
    }

    public Condition(
            String type,
            Pid imposer
    ) {
        this.type = notNull(type);
        this.imposer = notNull(imposer);
        this.addEvent(new ConditionCreatedEvent(this.getConditionId()));
    }

    public ConditionId getConditionId() {
        return conditionId;
    }

    public String getType() {
        return type;
    }

    public Pid getOwner() {
        return owner;
    }

    public Pid getSupervisor() {
        return supervisor;
    }

    public Pid getImposer() {
        return imposer;
    }

    public Country getBookingLocation() {
        return bookingLocation;
    }

    public void setBookingLocation(Country bookingLocation) {
        this.bookingLocation = bookingLocation;
    }

    public Fulfillment startNewFulfillment() {
        if (this.status != Status.OPEN) {
            throw new IllegalStateException("Cannot open fulfillment if condition is not OPEN");
        }
        return Fulfillment.createFulfillment(this.conditionId, this.fulfillmentReviewRequired);
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public Status getStatus() {
        return status;
    }

    public boolean isFulfillmentReviewRequired() {
        return fulfillmentReviewRequired;
    }

    public Long getVersion() {
        return version;
    }

    public Metadata getMetadata() {
        this.metadata.setCondition(this);
        return metadata;
    }

    public void changeOwner(String aPid) {

        //todo: block update when condition is OPEN, PENDING ?
        LOGGER.info("Changing owner in condition {} to {}", this.conditionId, aPid);
        if (this.status == Status.PENDING) {
            this.addEvent(new OwnerChangedEvent(this.owner, new Pid(aPid), this.conditionId));
        }
        this.owner = new Pid(aPid);
    }

    public void retire() {
        if (this.status != Status.OPEN) {
            throw new IllegalArgumentException();
        }
        LOGGER.info("Retiring condition {}", this.conditionId);
        this.status = Status.RETIRED;
        throw new RuntimeException("no event produced");
    }

    public void cancel() {
        if (this.status != Status.PENDING) {
            throw new IllegalArgumentException();
        }
        LOGGER.info("Cancelling condition {}", this.conditionId);
        this.status = Status.CANCELLED;
        this.addEvent(new ConditionCancelledEvent(this.conditionId));
    }

    public void discard() {
        if (this.status != Status.DRAFT) {
            throw new IllegalArgumentException();
        }
        LOGGER.info("Discarding condition {}", this.conditionId);
        this.status = Status.DISCARDED;
        this.addEvent(new ConditionDiscardedEvent(this.conditionId));
    }

    /*
    rename finish ?
     */
    public void close() {
        if (this.status != Status.OPEN) {
            throw new IllegalArgumentException();
        }
        LOGGER.info("Closing condition {}", this.conditionId);
        this.status = Status.DONE;
        this.addEvent(new ConditionClosedEvent(this.conditionId));
    }

    public void open() {
        /*
        if (this.status != Status.PENDING) {
            throw new IllegalArgumentException();
        }

         */
        LOGGER.info("Opening condition {}", this.conditionId);

        notNull(this.owner, () -> new IllegalArgumentException("Owner cannot be null"));
        notNull(this.bookingLocation, () -> new IllegalArgumentException("Booking location cannot be null"));

        this.status = Status.OPEN;
        this.addEvent(new ConditionOpenedEvent(this.conditionId));
    }

    public enum Status {
        DRAFT,
        PENDING,
        OPEN,
        DONE,
        RETIRED,
        CANCELLED,
        DISCARDED
    }

    public enum Classification {
        ASSET,
        INVESTMENT
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Condition condition = (Condition) o;
        return conditionId.equals(condition.conditionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conditionId);
    }

}
