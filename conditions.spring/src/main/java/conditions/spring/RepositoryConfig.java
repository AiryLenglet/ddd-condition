package conditions.spring;

import conditions.business_audit_trail.repository.BusinessAuditTrailConditionRepository;
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
import conditions.spring.repository.ConditionRepositoryImpl;
import conditions.spring.repository.FulfillmentRepositoryImpl;
import conditions.spring.repository.RequestCachedUserRepository;
import conditions.spring.repository.TaskRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;

@Configuration
public class RepositoryConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryConfig.class);

    @Bean
    public ConditionRepository conditionRepository(
            EventBus eventBus,
            UserRepository userRepository,
            UserProvider userProvider,
            BusinessAuditTrailConditionRepository.BusinessAuditTrail businessAuditTrail,
            EntityManager entityManager
    ) {
        return new IamConditionRepository(
                new BusinessAuditTrailConditionRepository(
                        new ConditionRepositoryImpl(eventBus, entityManager),
                        businessAuditTrail,
                        userProvider),
                userRepository,
                userProvider
        );
    }

    @Bean
    public TaskRepository taskRepository(
            EventBus eventBus,
            EntityManager entityManager
    ) {
        return new TaskRepositoryImpl(eventBus, entityManager);
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
        return new RequestCachedUserRepository(
                new UserRepository() {
                    @Override
                    public User findById(Pid aPid) {
                        return new User() {
                            @Override
                            public boolean isApprover() {
                                return true;
                            }

                            @Override
                            public Country location() {
                                return null;
                            }

                            @Override
                            public boolean hasBookingCenter(Country country) {
                                return false;
                            }
                        };
                    }
                }
        );
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public CacheManager requestScopedCacheManager() {
        return new ConcurrentMapCacheManager();
    }
}
