package conditions.spring.repository;

import conditions.core.event.Event;
import conditions.core.event.EventBus;
import conditions.core.model.draft.EscalationStep;
import conditions.core.repository.EscalationStepRepository;
import org.springframework.stereotype.Service;

@Service
public class EscalationStepRepositoryImpl implements EscalationStepRepository {

    private final SpringEscalationStepRepository springConditionRepository;
    private final EventBus eventBus;

    public EscalationStepRepositoryImpl(
            SpringEscalationStepRepository escalationStepRepository,
            EventBus eventBus
    ) {
        this.springConditionRepository = escalationStepRepository;
        this.eventBus = eventBus;
    }

    @Override
    public void save(EscalationStep escalationStep) {
        Event event;
        //handling before or after saving ?
        while ((event = escalationStep.pollEvent()) != null) {
            this.eventBus.publish(event);
        }
        this.springConditionRepository.save(escalationStep);
    }
}
