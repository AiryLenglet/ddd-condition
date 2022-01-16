package conditions.spring.controller;

import conditions.core.model.*;
import conditions.core.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    void test() {
        final var task = new FulfillmentTask(new ConditionId(), new FulfillmentId(), new Pid("123456"));
        this.taskRepository.save(task);
        LOGGER.info("{}", task.getTaskId());
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
