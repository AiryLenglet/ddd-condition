package conditions.spring.controller;

import conditions.core.model.*;
import conditions.core.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class Controller {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    void test() {
        final var task = new FulfillmentTask(new ConditionId(), new FulfillmentId(), new Pid("123456"));
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
}
