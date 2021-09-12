package conditions.core.model.draft;

import conditions.core.model.Aggregate;
import conditions.core.model.ConditionId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;

@Entity
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EscalationStep extends Aggregate {

    @EmbeddedId
    private EscalationStepId escalationStepId = new EscalationStepId();
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "CONDITION_ID"))
    })
    private ConditionId conditionId;

    public EscalationStep(ConditionId conditionId) {
        this.conditionId = conditionId;
    }

    public void close() {

    }

    public void changeCondition() {

    }
}
