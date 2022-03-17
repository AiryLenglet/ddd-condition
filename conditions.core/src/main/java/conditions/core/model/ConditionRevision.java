package conditions.core.model;

import javax.persistence.*;
import java.time.Instant;

import static conditions.common.util.Validate.notNull;

@Entity
public class ConditionRevision {

    @EmbeddedId
    private ConditionRevisionId id = new ConditionRevisionId();
    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "conditionId"))
    private ConditionId conditionId;
    @Embedded
    @AttributeOverride(name = "pid.value", column = @Column(name = "owner"))
    private Owner owner;
    private Long version;
    private Instant auditTime;

    ConditionRevision() {
    }

    public ConditionRevision(
            ConditionId conditionId,
            Owner owner,
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

    public Owner getOwner() {
        return owner;
    }

    public Long getVersion() {
        return version;
    }

    public Instant getAuditTime() {
        return auditTime;
    }
}
