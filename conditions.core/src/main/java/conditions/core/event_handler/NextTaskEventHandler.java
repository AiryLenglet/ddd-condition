package conditions.core.event_handler;

import conditions.core.event.Handler;
import conditions.core.event.TaskEvent;
import conditions.core.model.Condition;
import conditions.core.model.task.Task;
import conditions.core.repository.ConditionRepository;
import conditions.core.repository.TaskRepository;

import java.util.function.BiFunction;

import static conditions.core.repository.ConditionRepository.Specifications.conditionId;

public class NextTaskEventHandler<T extends TaskEvent> implements Handler<T> {

    private final ConditionRepository conditionRepository;
    private final TaskRepository taskRepository;
    private final BiFunction<Condition, T, Task> taskProducer;

    public NextTaskEventHandler(
            ConditionRepository conditionRepository,
            TaskRepository taskRepository,
            BiFunction<Condition, T, Task> taskProducer
    ) {
        this.conditionRepository = conditionRepository;
        this.taskRepository = taskRepository;
        this.taskProducer = taskProducer;
    }

    @Override
    public void handle(T event) {
        final var condition = this.conditionRepository.findOne(conditionId(event.conditionId()));
        this.taskRepository.save(this.taskProducer.apply(condition, event));
    }
}
