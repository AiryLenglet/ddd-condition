package conditions.spring;

import conditions.core.event.EventBus;
import conditions.core.model.Pid;
import conditions.core.repository.ConditionRepository;
import conditions.iam.model.User;
import conditions.iam.repository.IamConditionRepository;
import conditions.iam.repository.UserRepository;
import conditions.spring.repository.ConditionRepositoryImpl;
import conditions.spring.repository.RequestCachedUserRepository;
import conditions.spring.repository.SpringConditionRepository;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

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
        return new RequestCachedUserRepository(
                new UserRepository() {
                    @Override
                    public User findById(Pid aPid) {
                        return new User() {
                            @Override
                            public boolean isApprover() {
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
