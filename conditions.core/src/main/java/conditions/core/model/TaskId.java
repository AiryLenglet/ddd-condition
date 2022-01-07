package conditions.core.model;

import javax.persistence.Embeddable;

@Embeddable
public class TaskId extends UuidId {

    public TaskId(String id) {
        super(id);
    }

    public TaskId() {
        super();
    }

    public static TaskId of(String id) {
        return new TaskId(id);
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
