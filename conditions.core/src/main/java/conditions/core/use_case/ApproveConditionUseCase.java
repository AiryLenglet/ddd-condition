package conditions.core.use_case;

import conditions.core.model.ConditionId;
import conditions.core.repository.ApprovalStepRepository;

import javax.transaction.Transactional;

public class ApproveConditionUseCase {

    private final ApprovalStepRepository approvalStepRepository;

    public ApproveConditionUseCase(
            ApprovalStepRepository approvalStepRepository
    ) {
        this.approvalStepRepository = approvalStepRepository;
    }

    @Transactional
    public void execute(String conditionId, Request request) {
        final var approvalStep = this.approvalStepRepository.findByConditionId(ConditionId.of(conditionId));
        approvalStep.setJustification(request.justification());
        if (request.action() == Action.ACCEPT) {
            approvalStep.accept();
        } else if (request.action() == Action.ASK_CHANGE) {
            approvalStep.askForChange();
        } else {
            throw new IllegalArgumentException();
        }
        this.approvalStepRepository.save(approvalStep);
    }

    public static enum Action {
        ACCEPT,
        ASK_CHANGE
    }

    public static record Request(
            String justification,
            Action action
    ) {
    }
}
