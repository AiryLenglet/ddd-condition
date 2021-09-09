package conditions.spring;

import conditions.core.repository.ConditionRepository;
import conditions.core.repository.FulfillmentRepository;
import conditions.core.use_case.CreateConditionUseCase;
import conditions.core.use_case.OpenConditionUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Autowired
    private FulfillmentRepository fulfillmentRepository;

    @Autowired
    private ConditionRepository conditionRepository;

    @Bean
    public CreateConditionUseCase createConditionUseCase() {
        return new CreateConditionUseCase(conditionRepository);
    }

    @Bean
    public OpenConditionUseCase openConditionUseCase() {
        return new OpenConditionUseCase(conditionRepository, fulfillmentRepository);
    }
}
