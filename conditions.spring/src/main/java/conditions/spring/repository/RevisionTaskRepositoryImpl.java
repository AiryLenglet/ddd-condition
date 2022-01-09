package conditions.spring.repository;

import conditions.core.model.RevisionTask;
import conditions.core.model.TaskId;
import conditions.core.repository.RevisionTaskRepository;
import org.springframework.stereotype.Service;

@Service
public class RevisionTaskRepositoryImpl implements RevisionTaskRepository {

    private final EnversTaskRevRepository enversTaskRevRepository;

    public RevisionTaskRepositoryImpl(
            EnversTaskRevRepository enversTaskRevRepository
    ) {
        this.enversTaskRevRepository = enversTaskRevRepository;
    }

    @Override
    public RevisionTask findRevision(TaskId taskId, long version) {
        return this.enversTaskRevRepository.findRevisions(taskId).stream()
                .skip(version)
                .findFirst()
                .map(rev -> new RevisionTask(rev.getEntity().getTaskId()))
                .orElseThrow(NullPointerException::new);
    }
}
