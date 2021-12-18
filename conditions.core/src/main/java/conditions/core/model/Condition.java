package conditions.core.model;

import conditions.common.util.Validate;
import conditions.core.event.condition.ConditionClosedEvent;
import conditions.core.event.condition.ConditionOpenedEvent;
import conditions.core.event.condition.ConditionSubmittedEvent;
import conditions.core.event.condition.OwnerChangedEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Entity
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Condition extends Aggregate {

    @EmbeddedId
    private ConditionId conditionId = new ConditionId();
    @Enumerated(EnumType.STRING)
    private Status status = Status.DRAFT;
    private String type;
    @Embedded
    @AttributeOverride(name = "pid.value", column = @Column(name = "owner"))
    private Owner owner;
    private boolean fulfillmentReviewRequired = true;
    private boolean isRecurring = false;
    private boolean isInEdition = true;
    @Embedded
    private Country bookingLocation;

    Condition() {
        //package-private for hibernate
    }

    public Condition(String type) {
        this.type = type;
    }

    public ConditionId getConditionId() {
        return conditionId;
    }

    public String getType() {
        return type;
    }

    public Owner getOwner() {
        return owner;
    }

    public Fulfillment startNewFulfillment() {
        return new Fulfillment(this.conditionId, this.fulfillmentReviewRequired);
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public void changeOwner(String aPid) {
        if (!isInEdition) {
            throw new IllegalArgumentException();
        }
        log.info("Changing owner in condition {} to {}", this.conditionId, aPid);
        if (this.status == Status.PENDING) {
            this.addEvent(new OwnerChangedEvent(this.owner.getPid().getValue(), aPid, this.conditionId));
        }
        this.owner = new Owner(new Pid(aPid));
    }

    public void retire() {
        if (this.status != Status.OPEN) {
            throw new IllegalArgumentException();
        }
        log.info("Retiring condition {}", this.conditionId);
        this.status = Status.RETIRED;
        throw new RuntimeException("no event produced");
    }

    public void cancel() {
        if (this.status != Status.PENDING) {
            throw new IllegalArgumentException();
        }
        log.info("Cancelling condition {}", this.conditionId);
        this.status = Status.CANCELLED;
    }

    public void discard() {
        if (this.status != Status.DRAFT) {
            throw new IllegalArgumentException();
        }
        log.info("Discarding condition {}", this.conditionId);
        this.status = Status.DISCARDED;
    }

    /*
    rename finish ?
     */
    public void close() {
        if (this.status != Status.OPEN) {
            throw new IllegalArgumentException();
        }
        log.info("Closing condition {}", this.conditionId);
        this.status = Status.DONE;
        this.addEvent(new ConditionClosedEvent(this.conditionId));
    }

    public void submit() {
        if (this.status != Status.DRAFT && !this.isInEdition) {
            throw new IllegalArgumentException();
        }
        log.info("Submitting condition {}", this.conditionId);
        this.status = Status.PENDING;
        this.addEvent(new ConditionSubmittedEvent(this.conditionId));
    }

    public void enableEdition() {
        if (this.status == Status.PENDING) {
            this.isInEdition = true;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void open() {
        if (this.status != Status.PENDING) {
            throw new IllegalArgumentException();
        }
        log.info("Opening condition {}", this.conditionId);

        Validate.notNull(this.owner, () -> new IllegalArgumentException("Owner cannot be null"));
        Validate.notNull(this.bookingLocation, () -> new IllegalArgumentException("Booking location cannot be null"));

        this.isInEdition = false;
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

}
