package conditions.core.model.draft;

import conditions.core.model.UuidId;

import javax.persistence.Embeddable;

@Embeddable
public class ApprovalStepId extends UuidId {

    public ApprovalStepId(String id) {
        super(id);
    }

    public ApprovalStepId() {
        super();
    }

    public static ApprovalStepId of(String id) {
        return new ApprovalStepId(id);
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
