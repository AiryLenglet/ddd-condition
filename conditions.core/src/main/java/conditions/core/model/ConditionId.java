package conditions.core.model;

import javax.persistence.Embeddable;

@Embeddable
public class ConditionId extends LongId {
    public ConditionId() {
        super();
    }

    public ConditionId(Long id) {
        super(id);
    }
}
