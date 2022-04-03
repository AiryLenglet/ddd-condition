package conditions.core.model.task;

import conditions.common.util.Validate;
import conditions.core.model.*;

import javax.persistence.*;

@Entity
@DiscriminatorColumn(name = "TYPE")
public abstract class Task extends Aggregate {

    @EmbeddedId
    protected TaskId taskId;
    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "conditionId"))
    protected ConditionId conditionId;
    @AttributeOverride(name = "id", column = @Column(name = "fulfillmentId"))
    protected FulfillmentId fulfillmentId;
    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "previousTaskId"))
    protected TaskId previousTaskId;
    @Enumerated(EnumType.STRING)
    protected Status status;
    protected String comment;
    @Embedded
    @AttributeOverride(name = "pid.value", column = @Column(name = "assignee"))
    protected Pid assignee;
    @Version
    private Long version;

    Task() {
        //package-private for hibernate
    }

    protected Task(
            ConditionId conditionId,
            FulfillmentId fulfillmentId,
            Pid assignee
    ) {
        this.conditionId = Validate.notNull(conditionId);
        this.fulfillmentId = Validate.notNull(fulfillmentId);
        this.assignee = Validate.notNull(assignee);
        this.status = Status.OPEN;
        this.taskId = new TaskId();
    }

    protected Task(
            ConditionId conditionId,
            FulfillmentId fulfillmentId,
            Pid assignee,
            TaskId previousTaskId
    ) {
        this(
                conditionId,
                fulfillmentId,
                assignee
        );
        this.previousTaskId = Validate.notNull(previousTaskId);
    }

    public TaskId getTaskId() {
        return taskId;
    }

    public ConditionId getConditionId() {
        return conditionId;
    }

    public FulfillmentId getFulfillmentId() {
        return fulfillmentId;
    }

    public Pid getAssignee() {
        return assignee;
    }

    public TaskId getPreviousTaskId() {
        return previousTaskId;
    }

    public Status getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }

    public Long getVersion() {
        return version;
    }

    public void updateComment(String comment) {
        this.comment = comment;
    }

    public void submit() {
        if (this.status != Status.OPEN) {
            throw new IllegalStateException("cannot submit non open task");
        }
        this.status = Status.DONE;
    }

    public void cancel() {
        if (this.status != Status.OPEN) {
            throw new IllegalStateException("cannot cancel non open task");
        }
        this.status = Status.CANCELED;
    }

    public enum Status {
        OPEN,
        DONE,
        CANCELED
    }
}
