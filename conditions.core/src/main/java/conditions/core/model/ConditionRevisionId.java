package conditions.core.model;

import javax.persistence.Embeddable;

@Embeddable
public class ConditionRevisionId extends UuidId {

    public ConditionRevisionId(String id) {
        super(id);
    }

    public ConditionRevisionId() {
        super();
    }

    public static ConditionRevisionId of(String id) {
        return new ConditionRevisionId(id);
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
