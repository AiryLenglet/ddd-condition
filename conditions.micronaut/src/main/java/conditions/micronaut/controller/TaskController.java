package conditions.micronaut.controller;

import conditions.core.model.*;
import conditions.core.repository.TaskRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import javax.transaction.Transactional;
import java.util.stream.Stream;

@Controller
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(
            TaskRepository taskRepository
    ) {
        this.taskRepository = taskRepository;
    }

    @Get
    @Transactional
    Stream<Task> test() {
        final var task = new FulfillmentTask(new ConditionId(), new FulfillmentId(), new Pid("123456"));
        task.updateComment("meow meow");
        this.taskRepository.save(task);
        return this.taskRepository.findAll(TaskRepository.Specifications.isOpen());
    }
}
