package conditions.micronaut.controller;

import conditions.api.model.CompleteTask;
import conditions.core.model.*;
import conditions.core.model.task.DecisionTask;
import conditions.core.repository.ConditionRepository;
import conditions.core.repository.ConditionRevisionRepository;
import conditions.core.repository.FulfillmentRepository;
import conditions.core.repository.TaskRepository;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;

import javax.transaction.Transactional;
import java.util.UUID;
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

    @Post("/conditions")
    @Status(HttpStatus.CREATED)
    @Transactional
    conditions.api.model.ConditionId createCondition() {
        new conditions.api.model.Condition();

        final var condition = new Condition("TYPE", new Pid("222222"));
        condition.changeOwner("333333");
        condition.setBookingLocation(new Country("FR"));
        this.conditionRepository.save(condition);

        return new conditions.api.model.ConditionId()
                .id(String.valueOf(condition.getConditionId().getId()));
    }

    @Get("/conditions/{conditionId}")
    @Transactional
    conditions.api.model.Condition getCondition(@PathVariable("conditionId") String conditionId) {
        final var condition = this.conditionRepository.findOne(ConditionRepository.Specifications.conditionId(conditionId), ConditionStatusProjection.class);
        final var openTask = this.taskRepository.findAll(TaskRepository.Specifications.isOpen().and(TaskRepository.Specifications.conditionId(conditionId)));
        return new conditions.api.model.Condition()
                .id(String.valueOf(condition.conditionId().getId()))
                .status(condition.status().name())
                .openTask(openTask.findFirst()
                        .map(t -> new conditions.api.model.Task()
                                .id(String.valueOf(t.getTaskId().getId()))
                                .assignee(t.getAssignee().getValue())
                        )
                        .orElse(null)
                );
    }

    @Put("/conditions/{conditionId}/discard")
    @Transactional
    void discardCondition(@PathVariable("conditionId") String conditionId) {
        final var condition = this.conditionRepository.findOne(ConditionRepository.Specifications.conditionId(conditionId));
        condition.discard();
        this.conditionRepository.save(condition);
    }

    @Put("/conditions/{conditionId}")
    @Transactional
    void updateCondition(@PathVariable("conditionId") String conditionId) {
        final var condition = this.conditionRepository.findOne(ConditionRepository.Specifications.conditionId(conditionId));
        condition.getMetadata().set("CID", Metadata.Type.CLIENT, "56339");
        this.conditionRepository.save(condition);
    }

    @Put("/tasks/{taskId}/complete")
    @Transactional
    void completeTask(
            @PathVariable("taskId") String taskId,
            @Body CompleteTask completeTaskDto
    ) {
        final var task = this.taskRepository.findOne(TaskRepository.Specifications.isOpen().and(TaskRepository.Specifications.id(taskId)));
        task.updateComment(completeTaskDto.getComment());
        if (task instanceof DecisionTask<?> decisionTask) {
            decisionTask.setOutcome(completeTaskDto.getDecision());
        }
        task.submit();
        this.taskRepository.save(task);
    }

    @Get("/conditions/hist")
    @Transactional
    Stream<ConditionRevision> conditionRevisions() {
        return this.conditionRevisionRepository.findAll(ConditionRevisionRepository.Specifications.all());
    }
}
