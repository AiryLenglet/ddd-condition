package conditions.core.use_case;

import conditions.core.model.Condition;
import conditions.core.repository.ConditionRepository;

import javax.transaction.Transactional;

public class CreateConditionUseCase {

    private final ConditionRepository conditionRepository;

    public CreateConditionUseCase(
            ConditionRepository conditionRepository
    ) {
        this.conditionRepository = conditionRepository;
    }

    public static record Response(
            String conditionId
    ) {
    }

    @Transactional
    public Response execute() {
        return new Response(
                this.conditionRepository.save(new Condition("transaction")).getConditionId().getId()
        );
    }
}
