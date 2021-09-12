package conditions.spring.repository;

import conditions.core.event.Event;
import conditions.core.event.EventBus;
import conditions.core.model.ConditionId;
import conditions.core.model.draft.ApprovalStep;
import conditions.core.model.draft.ApprovalStepId;
import conditions.core.repository.ApprovalStepRepository;
import org.springframework.stereotype.Service;

@Service
public class ApprovalStepRepositoryImpl implements ApprovalStepRepository {

    private final SpringApprovalStepRepository approvalStepRepository;
    private final EventBus eventBus;

    public ApprovalStepRepositoryImpl(
            SpringApprovalStepRepository approvalStepRepository,
            EventBus eventBus
    ) {
        this.approvalStepRepository = approvalStepRepository;
        this.eventBus = eventBus;
    }

    @Override
    public void save(ApprovalStep approvalStep) {
        Event event;
        //handling before or after saving ?
        while ((event = approvalStep.pollEvent()) != null) {
            this.eventBus.publish(event);
        }
        this.approvalStepRepository.save(approvalStep);
    }

    @Override
    public ApprovalStep findById(ApprovalStepId approvalStepId) {
        return this.approvalStepRepository.getById(approvalStepId);
    }

    @Override
    public ApprovalStep findByConditionId(ConditionId conditionId) {
        return this.approvalStepRepository.findByConditionId(conditionId);
    }
}
