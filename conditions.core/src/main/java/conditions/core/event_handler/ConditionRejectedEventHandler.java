package conditions.core.event_handler;

import conditions.core.event.EventBus;
import conditions.core.event.approval.ConditionRejectedEvent;
import conditions.core.model.draft.EscalationStep;
import conditions.core.repository.EscalationStepRepository;

public class ConditionRejectedEventHandler implements EventBus.Handler<ConditionRejectedEvent> {

    private final EscalationStepRepository escalationStepRepository;

    public ConditionRejectedEventHandler(
            EscalationStepRepository escalationStepRepository
    ) {
        this.escalationStepRepository = escalationStepRepository;
    }

    @Override
    public void handle(ConditionRejectedEvent event) {
        this.escalationStepRepository.save(new EscalationStep(event.getConditionId()));
    }
}
