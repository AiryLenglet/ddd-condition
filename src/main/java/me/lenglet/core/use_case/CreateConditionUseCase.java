package me.lenglet.core.use_case;

import lombok.Builder;
import lombok.Value;
import me.lenglet.core.model.Condition;
import me.lenglet.core.repository.ConditionRepository;

import javax.transaction.Transactional;

public class CreateConditionUseCase {

    private final ConditionRepository conditionRepository;

    public CreateConditionUseCase(
            ConditionRepository conditionRepository
    ) {
        this.conditionRepository = conditionRepository;
    }

    @Builder
    @Value
    public static class Response {
        String conditionId;
    }

    @Transactional
    public Response execute() {
        return Response.builder()
                .conditionId(this.conditionRepository.save(new Condition("transaction")).getConditionId().getId())
                .build();
    }
}
