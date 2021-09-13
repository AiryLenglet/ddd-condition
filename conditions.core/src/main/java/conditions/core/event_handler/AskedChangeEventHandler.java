package conditions.core.event_handler;

import conditions.core.event.EventBus;
import conditions.core.event.approval.AskedChangeEvent;
import conditions.core.repository.ConditionRepository;

public class AskedChangeEventHandler implements EventBus.Handler<AskedChangeEvent> {

    private final ConditionRepository conditionRepository;

    public AskedChangeEventHandler(
            ConditionRepository conditionRepository
    ) {
        this.conditionRepository = conditionRepository;
    }

    @Override
    public void handle(AskedChangeEvent event) {
        final var condition = this.conditionRepository.findById(event.getConditionId());
        condition.enableEdition();
        this.conditionRepository.save(condition);
    }
}
