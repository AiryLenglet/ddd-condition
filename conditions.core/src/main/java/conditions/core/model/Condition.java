package conditions.core.model;

import conditions.core.event.condition.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ParamDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.Objects;

import static conditions.common.util.Validate.notNull;

@Entity
@FilterDef(
        name = "crossBorder",
        parameters = {
                @ParamDef(name = "ASSET_LOCATION_BLACKLIST", type = "string"),
                @ParamDef(name = "INVESTMENT_LOCATION_BLACKLIST", type = "string")
        },
        defaultCondition = """
                CLASSIFICATION is null
                or ( CLASSIFICATION = 'ASSET' and BOOKING_LOCATION not in (:ASSET_LOCATION_BLACKLIST) )
                or ( CLASSIFICATION = 'INVESTMENT' and BOOKING_LOCATION not in (:INVESTMENT_LOCATION_BLACKLIST) )
                """
)
@Filter(name = "crossBorder")
public class Condition extends Aggregate {

    private static final Logger LOGGER = LoggerFactory.getLogger(Condition.class);

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
    private ConditionId conditionId;
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
    @AttributeOverride(name = "code", column = @Column(name = "booking_location"))
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
        this.addEvent(new ConditionCreatedEvent(this));
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
