package conditions.core.use_case;

import lombok.Builder;
import lombok.Value;
import conditions.core.model.ConditionId;
import conditions.core.repository.ConditionRepository;
import conditions.core.repository.FulfillmentRepository;

import javax.transaction.Transactional;

public class OpenConditionUseCase {

    private final ConditionRepository conditionRepository;
    private final FulfillmentRepository fulfillmentRepository;

    public OpenConditionUseCase(
            ConditionRepository conditionRepository,
            FulfillmentRepository fulfillmentRepository
    ) {
        this.conditionRepository = conditionRepository;
        this.fulfillmentRepository = fulfillmentRepository;
    }

    @Builder
    @Value
    public static class Request {
        String conditionId;
    }

    @Transactional
    public void execute(Request request) {
        final var condition = this.conditionRepository.findById(ConditionId.of(request.getConditionId()));
        condition.open();
        this.conditionRepository.save(condition);
    }
}
