package conditions.spring.repository;

import conditions.core.event.Event;
import conditions.core.event.EventBus;
import conditions.core.model.Condition;
import conditions.core.model.ConditionId;
import conditions.core.repository.ConditionRepository;

public class ConditionRepositoryImpl implements ConditionRepository {

    private final SpringConditionRepository springConditionRepository;
    private final EventBus eventBus;

    public ConditionRepositoryImpl(
            SpringConditionRepository springConditionRepository,
            EventBus eventBus
    ) {
        this.springConditionRepository = springConditionRepository;
        this.eventBus = eventBus;
    }

    @Override
    public void save(Condition condition) {
        Event event;
        //handling before or after saving ?
        while ((event = condition.pollEvent()) != null) {
            this.eventBus.publish(event);
        }
        this.springConditionRepository.save(condition);
    }

    @Override
    public Condition findById(ConditionId id) {
        return this.springConditionRepository.getById(id);
    }

}
