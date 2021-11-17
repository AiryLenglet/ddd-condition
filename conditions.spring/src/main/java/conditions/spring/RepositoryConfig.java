package conditions.spring;

import conditions.core.event.EventBus;
import conditions.core.repository.ConditionRepository;
import conditions.iam.model.User;
import conditions.iam.repository.IamConditionRepository;
import conditions.iam.repository.UserRepository;
import conditions.spring.repository.ConditionRepositoryImpl;
import conditions.spring.repository.SpringConditionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public ConditionRepository conditionRepository(
            SpringConditionRepository springConditionRepository,
            EventBus eventBus,
            UserRepository userRepository
    ) {
        return new IamConditionRepository(
                new ConditionRepositoryImpl(springConditionRepository, eventBus),
                userRepository);
    }

    @Bean
    public UserRepository mockUserRepository() {
        return new UserRepository() {
            @Override
            public User findById(String id) {
                return new User() {
                    @Override
                    public boolean isApprover() {
                        return false;
                    }
                };
            }
        };
    }
}
