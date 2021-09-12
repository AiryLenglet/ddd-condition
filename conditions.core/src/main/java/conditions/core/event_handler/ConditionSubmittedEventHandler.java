package conditions.core.event_handler;

import conditions.core.event.EventBus;
import conditions.core.event.condition.ConditionSubmittedEvent;
import conditions.core.model.draft.ApprovalStep;
import conditions.core.repository.ApprovalStepRepository;

public class ConditionSubmittedEventHandler implements EventBus.Handler<ConditionSubmittedEvent> {

    private final ApprovalStepRepository approvalStepRepository;

    public ConditionSubmittedEventHandler(
            ApprovalStepRepository approvalStepRepository
    ) {
        this.approvalStepRepository = approvalStepRepository;
    }

    @Override
    public void handle(ConditionSubmittedEvent event) {
        this.approvalStepRepository.save(new ApprovalStep(event.getConditionId()));
    }
}
