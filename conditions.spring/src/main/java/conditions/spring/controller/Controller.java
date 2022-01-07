package conditions.spring.controller;

import conditions.core.model.ConditionId;
import conditions.core.model.FulfillmentTask;
import conditions.core.model.TaskId;
import conditions.core.repository.TaskRepository;
import conditions.core.use_case.ApproveConditionUseCase;
import conditions.core.use_case.CreateConditionUseCase;
import conditions.core.use_case.Request;
import conditions.core.use_case.SubmitConditionUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class Controller {

    @Autowired
    private CreateConditionUseCase createConditionUseCase;
    @Autowired
    private SubmitConditionUseCase submitConditionUseCase;
    @Autowired
    private ApproveConditionUseCase approveConditionUseCase;
    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    void test() {
        final var task = new FulfillmentTask(new ConditionId(), null);
        this.taskRepository.save(task);
        log.info("{}", task.getTaskId());
    }

    @GetMapping(path = "/{id}")
    public void getget(
            @PathVariable("id") String id
    ) {
        final var task = this.taskRepository.findById(TaskId.of(id));
        task.updateComment("everything is alright");
        task.submit();
        this.taskRepository.save(task);
    }

    @PostMapping
    public CreateConditionUseCase.Response create() {
        return this.createConditionUseCase.execute();
    }

    @PostMapping(path = "/{id}/submit")
    public void submit(
            @PathVariable("id") String id
    ) {
        this.submitConditionUseCase.execute(new Request(id));
    }

    @PostMapping(path = "/{id}/approve")
    public void approve(
            @PathVariable("id") String id,
            @RequestBody ApproveConditionUseCase.Request body
    ) {
        this.approveConditionUseCase.execute(id, body);
    }

    @PutMapping(path = "/{id}")
    public void open(@PathVariable("id") String id) {
        this.submitConditionUseCase.execute(new Request(id));
    }
}
