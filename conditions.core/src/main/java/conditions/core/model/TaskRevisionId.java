package conditions.core.model;

import javax.persistence.Embeddable;

@Embeddable
public class TaskRevisionId extends LongId {

    public TaskRevisionId() {
        super();
    }

    public TaskRevisionId(Long id) {
        super(id);
    }
}
