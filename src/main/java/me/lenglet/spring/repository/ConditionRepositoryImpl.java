package me.lenglet.spring.repository;

import me.lenglet.core.event.Event;
import me.lenglet.core.event.EventBus;
import me.lenglet.core.model.Condition;
import me.lenglet.core.model.ConditionId;
import me.lenglet.core.repository.ConditionRepository;
import org.springframework.stereotype.Service;

@Service
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
    public Condition save(Condition condition) {
        Event event;
        //handling before or after saving ?
        while ((event = condition.pollEvent()) != null) {
            this.eventBus.publish(event);
        }
        return this.springConditionRepository.save(condition);
    }

    @Override
    public Condition findById(ConditionId id) {
        return this.springConditionRepository.getById(id);
    }

}
