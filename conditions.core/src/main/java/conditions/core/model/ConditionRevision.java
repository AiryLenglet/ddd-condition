package conditions.core.model;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

import static conditions.common.util.Validate.notNull;

@Entity
@FilterDef(
        name = "involvedInCondition",
        parameters = {@ParamDef(name = "user", type = "string")},
        defaultCondition = """
                :user in (
                    select c.OWNER, c.IMPOSER from condition c
                    where c.ID = CONDITION_ID
                )
                """
)
@Filter(name = "involvedInCondition")
public class ConditionRevision {

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
    private ConditionRevisionId id;
    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "conditionId"))
    private ConditionId conditionId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "owner"))
    private Pid owner;
    private Long version;
    private Instant auditTime;

    ConditionRevision() {
    }

    public ConditionRevision(
            ConditionId conditionId,
            Pid owner,
            Long version,
            Instant auditTime
    ) {
        this.conditionId = notNull(conditionId);
        this.owner = owner;
        this.version = notNull(version);
        this.auditTime = notNull(auditTime);
    }

    public ConditionRevisionId getId() {
        return id;
    }

    public ConditionId getConditionId() {
        return conditionId;
    }

    public Pid getOwner() {
        return owner;
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
        ConditionRevision condition = (ConditionRevision) o;
        return id.equals(condition.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
