package conditions.spring.repository;

import conditions.core.event.Event;
import conditions.core.event.EventBus;
import conditions.core.model.Task;
import conditions.core.model.TaskId;
import conditions.core.repository.Specification;
import conditions.core.repository.TaskRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.stream.Stream;

@Service
public class TaskRepositoryImpl implements TaskRepository {

    private final SpringTaskRepository springTaskRepository;
    private final EventBus eventBus;
    private final EntityManager entityManager;

    public TaskRepositoryImpl(
            SpringTaskRepository springTaskRepository,
            EventBus eventBus,
            EntityManager entityManager
    ) {
        this.springTaskRepository = springTaskRepository;
        this.eventBus = eventBus;
        this.entityManager = entityManager;
    }

    @Override
    public void save(Task task) {
        Event event;
        //handling before or after saving ?
        while ((event = task.pollEvent()) != null) {
            this.eventBus.publish(event);
        }
        this.springTaskRepository.save(task);
    }

    @Override
    public Task findById(TaskId id) {
        return this.springTaskRepository.getById(id);
    }

    @Override
    public Stream<Task> findAll(Specification<Task> specification) {
        final var criteriaBuilder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<Task> query = criteriaBuilder.createQuery(Task.class);
        final Root<Task> root = query.from(Task.class);

        query.where(specification.toPredicate(root, query, criteriaBuilder));

        return this.entityManager.createQuery(query).getResultList().stream();
    }
}
