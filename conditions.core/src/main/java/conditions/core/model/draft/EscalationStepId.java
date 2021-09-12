package conditions.core.model.draft;

import conditions.core.model.UuidId;

import javax.persistence.Embeddable;

@Embeddable
public class EscalationStepId extends UuidId {

    public EscalationStepId(String id) {
        super(id);
    }

    public EscalationStepId() {
        super();
    }

    public static EscalationStepId of(String id) {
        return new EscalationStepId(id);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
