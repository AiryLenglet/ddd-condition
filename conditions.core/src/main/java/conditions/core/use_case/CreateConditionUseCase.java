package conditions.core.use_case;

import conditions.core.model.Condition;
import conditions.core.model.Pid;
import conditions.core.repository.ConditionRepository;

import javax.transaction.Transactional;

public class CreateConditionUseCase {

    private final ConditionRepository conditionRepository;

    public CreateConditionUseCase(
            ConditionRepository conditionRepository
    ) {
        this.conditionRepository = conditionRepository;
    }

    @Transactional
    public Response execute() {
        final var condition = new Condition("transaction", new Pid("222222"));
        this.conditionRepository.persist(condition);
        return new Response(String.valueOf(condition.getConditionId().getId()));
    }

    public static record Response(
            String conditionId
    ) {
    }
}
