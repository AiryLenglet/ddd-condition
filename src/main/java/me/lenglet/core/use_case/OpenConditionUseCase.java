package me.lenglet.core.use_case;

import lombok.Builder;
import lombok.Value;
import me.lenglet.core.model.ConditionId;
import me.lenglet.core.repository.ConditionRepository;
import me.lenglet.core.repository.FulfillmentRepository;

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
