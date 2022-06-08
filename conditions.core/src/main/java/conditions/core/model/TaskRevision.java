package conditions.core.model;

import conditions.core.model.task.Task;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

import static conditions.common.util.Validate.notNull;

@Entity
public class TaskRevision {

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
    private TaskRevisionId id;
    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "taskId"))
    private TaskId taskId;
    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "conditionId"))
    private ConditionId conditionId;
    @AttributeOverride(name = "id", column = @Column(name = "fulfillmentId"))
    private FulfillmentId fulfillmentId;
    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "previousTaskId"))
    private TaskId previousTaskId;
    @Enumerated(EnumType.STRING)
    private Task.Status status;
    private String comment;
    @Embedded
    @AttributeOverride(name = "pid.value", column = @Column(name = "assignee"))
    private Pid assignee;
    private String outcome;
    private Long version;
    private Instant auditTime;

    TaskRevision() {
    }

    public TaskRevision(
            TaskId taskId,
            ConditionId conditionId,
            FulfillmentId fulfillmentId,
            TaskId previousTaskId,
            Task.Status status,
            String comment,
            Pid assignee,
            String outcome,
            Long version,
            Instant auditTime
    ) {
        this.taskId = notNull(taskId);
        this.conditionId = conditionId;
        this.fulfillmentId = fulfillmentId;
        this.previousTaskId = previousTaskId;
        this.status = status;
        this.comment = comment;
        this.assignee = assignee;
        this.outcome = outcome;
        this.version = notNull(version);
        this.auditTime = notNull(auditTime);
    }

    public TaskRevisionId getId() {
        return id;
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

    public TaskId getPreviousTaskId() {
        return previousTaskId;
    }

    public Task.Status getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }

    public Pid getAssignee() {
        return assignee;
    }

    public String getOutcome() {
        return outcome;
    }

    public Long getVersion() {
        return version;
    }

    public Instant getAuditTime() {
        return auditTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskRevision task = (TaskRevision) o;
        return id.equals(task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
