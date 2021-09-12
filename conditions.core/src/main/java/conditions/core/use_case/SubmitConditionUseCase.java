package conditions.core.use_case;

import conditions.core.model.ConditionId;
import conditions.core.repository.ConditionRepository;

import javax.transaction.Transactional;

public class SubmitConditionUseCase {

    private final ConditionRepository conditionRepository;

    public SubmitConditionUseCase(
            ConditionRepository conditionRepository
    ) {
        this.conditionRepository = conditionRepository;
    }

    @Transactional
    public void execute(Request request) {
        final var condition = this.conditionRepository.findById(ConditionId.of(request.conditionId()));
        condition.submit();
        this.conditionRepository.save(condition);
    }
}
