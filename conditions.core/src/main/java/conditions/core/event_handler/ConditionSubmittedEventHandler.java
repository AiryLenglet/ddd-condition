package conditions.core.event_handler;

import conditions.core.event.EventBus;
import conditions.core.event.condition.ConditionSubmittedEvent;
import conditions.core.factory.Clock;
import conditions.core.model.draft.ApprovalStep;
import conditions.core.repository.ApprovalStepRepository;

public class ConditionSubmittedEventHandler implements EventBus.Handler<ConditionSubmittedEvent> {

    private final ApprovalStepRepository approvalStepRepository;
    private final Clock clock;

    public ConditionSubmittedEventHandler(
            ApprovalStepRepository approvalStepRepository,
            Clock clock
    ) {
        this.approvalStepRepository = approvalStepRepository;
        this.clock = clock;
    }

    @Override
    public void handle(ConditionSubmittedEvent event) {
        this.approvalStepRepository.save(new ApprovalStep(event.getConditionId(), clock.now()));
    }
}
