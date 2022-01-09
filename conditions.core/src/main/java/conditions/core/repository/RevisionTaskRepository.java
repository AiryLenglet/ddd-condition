package conditions.core.repository;

import conditions.core.model.RevisionTask;
import conditions.core.model.TaskId;

public interface RevisionTaskRepository {
    RevisionTask findRevision(TaskId taskId, long version);
}
