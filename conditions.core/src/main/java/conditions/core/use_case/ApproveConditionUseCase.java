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
    public void execute(String approvalId) {
        final var approvalStep = this.approvalStepRepository.findByConditionId(ConditionId.of(approvalId));
        approvalStep.setJustification("all os ok");
        approvalStep.accept();
        this.approvalStepRepository.save(approvalStep);
    }
}
