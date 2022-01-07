package conditions.core.model;

import conditions.common.util.Validate;

import javax.persistence.*;

@Entity
@DiscriminatorColumn(name = "TYPE")
public abstract class Task<E extends Enum<E>> extends Aggregate {

    @EmbeddedId
    protected TaskId taskId;
    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "conditionId"))
    protected ConditionId conditionId;
    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "previousTaskId"))
    protected TaskId previousTaskId;
    @Enumerated(EnumType.STRING)
    protected Status status;
    protected String comment;
    @Enumerated(EnumType.STRING)
    protected E outcome;

    Task() {
        //package-private for hibernate
    }

    public Task(
            ConditionId conditionId,
            TaskId previousTaskId
    ) {
        this.conditionId = Validate.notNull(conditionId);
        this.previousTaskId = previousTaskId;
        this.status = Status.OPEN;
        this.taskId = new TaskId();
    }

    public TaskId getTaskId() {
        return taskId;
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
