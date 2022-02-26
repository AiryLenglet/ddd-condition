package conditions.spring.controller;

import conditions.core.model.*;
import conditions.core.repository.ConditionRepository;
import conditions.core.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

import static conditions.core.repository.TaskRepository.Specifications.id;

@RestController
public class Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ConditionRepository conditionRepository;

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
        final var task = this.taskRepository.findOne(id(TaskId.of(id)));
        task.updateComment("everything is alright");
        task.submit();
        this.taskRepository.save(task);
    }

    @Transactional
    @PostMapping
    public void createCondition() {
        final var condtiions = this.conditionRepository.findAll(
                (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("conditionId")))
                .collect(Collectors.toList());

        final var condition = new Condition("jack");
        condition.changeOwner("456789");
        this.conditionRepository.save(condition);
    }
}
