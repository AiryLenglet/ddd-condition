package conditions.spring;

import conditions.core.repository.ApprovalStepRepository;
import conditions.core.repository.ConditionRepository;
import conditions.core.repository.FulfillmentRepository;
import conditions.core.use_case.ApproveConditionUseCase;
import conditions.core.use_case.CreateConditionUseCase;
import conditions.core.use_case.SubmitConditionUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Autowired
    private FulfillmentRepository fulfillmentRepository;
    @Autowired
    private ConditionRepository conditionRepository;
    @Autowired
    private ApprovalStepRepository approvalStepRepository;

    @Bean
    public CreateConditionUseCase createConditionUseCase() {
        return new CreateConditionUseCase(conditionRepository);
    }

    @Bean
    public SubmitConditionUseCase openConditionUseCase() {
        return new SubmitConditionUseCase(conditionRepository);
    }

    @Bean
    public ApproveConditionUseCase approveConditionUseCase() {
        return new ApproveConditionUseCase(approvalStepRepository);
    }
}
