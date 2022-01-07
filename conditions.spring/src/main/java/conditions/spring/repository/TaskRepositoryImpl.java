package conditions.spring.repository;

import conditions.core.event.Event;
import conditions.core.event.EventBus;
import conditions.core.model.Task;
import conditions.core.model.TaskId;
import conditions.core.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskRepositoryImpl implements TaskRepository {

    private final SpringTaskRepository springTaskRepository;
    private final EventBus eventBus;

    public TaskRepositoryImpl(
            SpringTaskRepository springTaskRepository,
            EventBus eventBus
    ) {
        this.springTaskRepository = springTaskRepository;
        this.eventBus = eventBus;
    }

    @Override
    public void save(Task<?> task) {
        Event event;
        //handling before or after saving ?
        while ((event = task.pollEvent()) != null) {
            this.eventBus.publish(event);
        }
        this.springTaskRepository.save(task);
    }

    @Override
    public Task<?> findById(TaskId id) {
        return this.springTaskRepository.getById(id);
    }
}
