package conditions.core.event_handler;

import conditions.core.event.EventBus;
import conditions.core.event.approval.ConditionAcceptedEvent;
import conditions.core.repository.ConditionRepository;

import static conditions.core.repository.ConditionRepository.Specifications.conditionId;

public class ConditionAcceptedEventHandler implements EventBus.Handler<ConditionAcceptedEvent> {

    private final ConditionRepository conditionRepository;

    public ConditionAcceptedEventHandler(
            ConditionRepository conditionRepository
    ) {
        this.conditionRepository = conditionRepository;
    }

    @Override
    public void handle(ConditionAcceptedEvent event) {
        final var condition = this.conditionRepository.findOne(conditionId(event.conditionId()));
        condition.open();
        this.conditionRepository.save(condition);
    }
}
