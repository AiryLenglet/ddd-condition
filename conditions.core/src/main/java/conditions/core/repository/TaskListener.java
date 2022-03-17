package conditions.core.repository;

import conditions.core.factory.Clock;
import conditions.core.model.DecisionTask;
import conditions.core.model.TaskRevision;
import conditions.core.model.Task;
import jakarta.inject.Singleton;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

@Singleton
public class TaskListener {

    private final TaskRevisionRepository taskRevisionRepository;
    private final Clock clock;

    public TaskListener(
            TaskRevisionRepository taskRevisionRepository,
            Clock clock
    ) {
        this.taskRevisionRepository = taskRevisionRepository;
        this.clock = clock;
    }

    @PostPersist
    @PostUpdate
    public void postPersist(Task task) {
        this.taskRevisionRepository.save(new TaskRevision(
                task.getTaskId(),
                task.getConditionId(),
                task.getFulfillmentId(),
                task.getPreviousTaskId(),
                task.getStatus(),
                task.getComment(),
                task.getAssignee(),
                task instanceof DecisionTask decisionTask ? decisionTask.getOutcome().name() : null,
                task.getVersion(),
                this.clock.now()
        ));
    }
}
