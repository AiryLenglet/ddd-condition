package conditions.core.model;

import javax.persistence.Embeddable;

@Embeddable
public class ConditionRevisionId extends LongId {

    public ConditionRevisionId() {
        super();
    }

    public ConditionRevisionId(Long id) {
        super(id);
    }
}
