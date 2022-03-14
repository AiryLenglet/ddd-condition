package conditions.micronaut.config;

import conditions.context.UserProvider;
import conditions.core.event.EventBus;
import conditions.core.model.Country;
import conditions.core.model.Pid;
import conditions.core.repository.ConditionRepository;
import conditions.core.repository.FulfillmentRepository;
import conditions.core.repository.TaskRepository;
import conditions.iam.model.User;
import conditions.iam.repository.IamConditionRepository;
import conditions.iam.repository.UserRepository;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

import javax.persistence.EntityManager;

@Factory
public class RepositoryFactory {

    @Bean
    public TaskRepository taskRepository(
            EventBus eventBus,
            EntityManager entityManager
    ) {
        return new TaskRepositoryImpl(eventBus, entityManager);
    }

    @Bean
    public ConditionRepository conditionRepository(
            EventBus eventBus,
            EntityManager entityManager,
            UserRepository userRepository,
            UserProvider userProvider
    ) {
        return new IamConditionRepository(
                new ConditionRepositoryImpl(eventBus, entityManager),
                userRepository,
                userProvider
        );
    }

    @Bean
    public FulfillmentRepository fulfillmentRepository(
            EventBus eventBus,
            EntityManager entityManager
    ) {
        return new FulfillmentRepositoryImpl(eventBus, entityManager);
    }

    @Bean
    public UserProvider mockUserProvider() {
        return () -> new Pid("123456");
    }

    @Bean
    public UserRepository mockUserRepository() {
        return new UserRepository() {
            @Override
            public User findById(Pid aPid) {
                return new User() {
                    @Override
                    public boolean isApprover() {
                        return true;
                    }

                    @Override
                    public Country location() {
                        return new Country("FR");
                    }

                    @Override
                    public boolean hasBookingCenter(Country country) {
                        return false;
                    }
                };
            }
        };
    }
}
