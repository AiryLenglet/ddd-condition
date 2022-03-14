package conditions.micronaut.controller;

import conditions.core.model.*;
import conditions.core.repository.ConditionRepository;
import conditions.core.repository.FulfillmentRepository;
import conditions.core.repository.TaskRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import javax.transaction.Transactional;
import java.util.stream.Stream;

@Controller
public class TaskController {

    private final TaskRepository taskRepository;
    private final ConditionRepository conditionRepository;
    private final FulfillmentRepository fulfillmentRepository;

    public TaskController(
            TaskRepository taskRepository,
            ConditionRepository conditionRepository,
            FulfillmentRepository fulfillmentRepository
    ) {
        this.taskRepository = taskRepository;
        this.conditionRepository = conditionRepository;
        this.fulfillmentRepository = fulfillmentRepository;
    }

    @Get
    @Transactional
    Stream<Task> test() {
        final var task = new FulfillmentTask(new ConditionId(), new FulfillmentId(), new Pid("123456"));
        task.updateComment("meow meow");
        this.taskRepository.save(task);
        return this.taskRepository.findAll(TaskRepository.Specifications.isOpen());
    }

    @Get("/conditions")
    @Transactional
    Stream<Condition> conditions() {
        final var condition = new Condition("TYPE");
        condition.setImposer(new Imposer(new Pid("222222")));
        this.conditionRepository.save(condition);

        this.fulfillmentRepository.save(condition.startNewFulfillment());

        return this.conditionRepository.findAll(ConditionRepository.Specifications.all());
    }
}
