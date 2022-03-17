package conditions.core.model;

import javax.persistence.Embeddable;

@Embeddable
public class TaskRevisionId extends UuidId {

    public TaskRevisionId(String id) {
        super(id);
    }

    public TaskRevisionId() {
        super();
    }

    public static TaskRevisionId of(String id) {
        return new TaskRevisionId(id);
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
