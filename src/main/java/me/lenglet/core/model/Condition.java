package me.lenglet.core.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import me.lenglet.core.event.condition.OwnerChangedEvent;
import me.lenglet.core.event.condition.ConditionClosedEvent;
import me.lenglet.core.event.condition.ConditionOpenedEvent;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Condition extends Aggregate {

    @EmbeddedId
    private ConditionId conditionId = new ConditionId();
    @Enumerated(EnumType.STRING)
    private Status status = Status.DRAFT;
    private String type;
    private String owner;
    private boolean fulfillmentReviewRequired = true;
    private boolean isRecurring = false;

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

    public String getOwner() {
        return owner;
    }

    public Fulfillment startNewFulfillment() {
        return new Fulfillment(this.conditionId, this.fulfillmentReviewRequired);
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public void changeOwner(String owner) {
        if (!(this.status == Status.DRAFT || this.status == Status.PENDING)) {
            throw new IllegalArgumentException();
        }
        if (this.status == Status.PENDING) {
            this.addEvent(new OwnerChangedEvent(this.owner, owner));
        }
        this.owner = owner;
    }

    public void retire() {
        if (this.status != Status.OPEN) {
            throw new IllegalArgumentException();
        }
        this.status = Status.RETIRED;

    }

    public void cancel() {
        if (this.status != Status.PENDING) {
            throw new IllegalArgumentException();
        }
        this.status = Status.CANCELLED;
    }

    public void discard() {
        if (this.status != Status.DRAFT) {
            throw new IllegalArgumentException();
        }
        this.status = Status.DISCARDED;
    }

    /*
    rename finish ?
     */
    public void close() {
        if (this.status != Status.OPEN) {
            throw new IllegalArgumentException();
        }
        this.status = Status.DONE;
        this.addEvent(new ConditionClosedEvent(this.conditionId));
    }

    public void open() {
        if (this.status != Status.DRAFT) {
            throw new IllegalArgumentException();
        }
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
