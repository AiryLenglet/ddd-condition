package conditions.reminder;

import conditions.core.model.Pid;
import conditions.core.model.Task;
import conditions.core.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class Reminder {

    private static final Logger LOGGER = LoggerFactory.getLogger(Reminder.class);

    private final TaskRepository taskRepository;

    public Reminder(
            TaskRepository taskRepository
    ) {
        this.taskRepository = taskRepository;
    }

    public void start() {
        this.taskRepository.findAll(TaskRepository.Specifications.isOpen())
                .collect(Collectors.groupingBy(Task::getAssignee))
                .forEach(this::produceSummaryReport);
    }

    private void produceSummaryReport(Pid pid, List<Task> tasks) {
        final var fulfillments = tasks.stream()
                .map(t -> t.getFulfillmentId())
                .distinct();
        LOGGER.info("action needed on {} for {}", fulfillments, pid);
    }
}
