package conditions.core.event_handler;

import conditions.core.event.EventBus;
import conditions.core.event.approval.ConditionAcceptedEvent;
import conditions.core.repository.ConditionRepository;

public class ConditionAcceptedEventHandler implements EventBus.Handler<ConditionAcceptedEvent> {

    private final ConditionRepository conditionRepository;

    public ConditionAcceptedEventHandler(
            ConditionRepository conditionRepository
    ) {
        this.conditionRepository = conditionRepository;
    }

    @Override
    public void handle(ConditionAcceptedEvent event) {
        final var condition = this.conditionRepository.findById(event.getConditionId());
        condition.open();
        this.conditionRepository.save(condition);
    }
}
