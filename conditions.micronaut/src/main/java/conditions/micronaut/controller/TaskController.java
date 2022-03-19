package conditions.micronaut.controller;

import conditions.core.model.*;
import conditions.core.repository.ConditionRepository;
import conditions.core.repository.ConditionRevisionRepository;
import conditions.core.repository.FulfillmentRepository;
import conditions.core.repository.TaskRepository;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;

import javax.transaction.Transactional;
import java.util.stream.Stream;

@Controller
public class TaskController {

    private final TaskRepository taskRepository;
    private final ConditionRepository conditionRepository;
    private final FulfillmentRepository fulfillmentRepository;
    private final ConditionRevisionRepository conditionRevisionRepository;

    public TaskController(
            TaskRepository taskRepository,
            ConditionRepository conditionRepository,
            FulfillmentRepository fulfillmentRepository,
            ConditionRevisionRepository conditionRevisionRepository
    ) {
        this.taskRepository = taskRepository;
        this.conditionRepository = conditionRepository;
        this.fulfillmentRepository = fulfillmentRepository;
        this.conditionRevisionRepository = conditionRevisionRepository;
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

    @Post("/conditions")
    @Status(HttpStatus.CREATED)
    @Transactional
    ConditionIdDto createCondition() {
        final var condition = new Condition("TYPE");
        condition.setImposer(new Imposer(new Pid("222222")));
        this.conditionRepository.save(condition);

        return new ConditionIdDto(condition.getConditionId().getId());
    }

    public record ConditionIdDto(
            String id
    ) {
    }

    public record ConditionDto(
            String id,
            TaskDto openTask
    ) {
    }

    public record TaskDto(
            String id,
            String assignee
    ) {
    }

    @Get("/conditions/{conditionId}")
    @Transactional
    ConditionDto getCondition(@PathVariable("conditionId") String conditionId) {
        final var condition = this.conditionRepository.findOne(ConditionRepository.Specifications.conditionId(conditionId));
        final var openTask = this.taskRepository.findAll(TaskRepository.Specifications.isOpen().and(TaskRepository.Specifications.conditionId(conditionId)));
        return new ConditionDto(
                condition.getConditionId().getId(),
                openTask.findFirst()
                        .map(t -> new TaskDto(
                                t.getTaskId().getId(),
                                t.getAssignee().getValue()
                        ))
                        .orElse(null)
        );
    }

    @Put("/conditions/{conditionId}/start")
    @Transactional
    void startCondition(@PathVariable("conditionId") String conditionId) {
        final var id = new ConditionId(conditionId);
        final var condition = this.conditionRepository.findOne(ConditionRepository.Specifications.conditionId(id));

        final var fulfillment = condition.startNewFulfillment();
        this.fulfillmentRepository.save(fulfillment);

    }

    @Put("/conditions/{conditionId}/discard")
    @Transactional
    void discardCondition(@PathVariable("conditionId") String conditionId) {
        final var condition = this.conditionRepository.findOne(ConditionRepository.Specifications.conditionId(conditionId));
        condition.discard();
        this.conditionRepository.save(condition);
    }

    @Get("/conditions/up")
    @Transactional
    void updateConditions() {
        this.conditionRepository.findAll(ConditionRepository.Specifications.all())
                .map(c -> {
                    c.changeOwner("123456");
                    return c;
                })
                .forEach(c -> this.conditionRepository.save(c));
    }

    record CompleteTaskDto(
            String comment
    ) {
    }

    @Put("/tasks/{taskId}/complete")
    @Transactional
    void completeTask(
            @PathVariable("taskId") String taskId,
            @Body CompleteTaskDto completeTaskDto
    ) {
        final var task = this.taskRepository.findOne(TaskRepository.Specifications.isOpen().and(TaskRepository.Specifications.id(taskId)));
        task.updateComment(completeTaskDto.comment());
        task.submit();
        this.taskRepository.save(task);
    }

    @Get("/conditions/hist")
    @Transactional
    Stream<ConditionRevision> conditionRevisions() {
        return this.conditionRevisionRepository.findAll(ConditionRevisionRepository.Specifications.all());
    }
}
