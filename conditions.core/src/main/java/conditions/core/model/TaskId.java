package conditions.core.model;

import javax.persistence.Embeddable;

@Embeddable
public class TaskId extends LongId {

    public TaskId() {
        super();
    }

    public TaskId(Long id) {
        super(id);
    }
}
