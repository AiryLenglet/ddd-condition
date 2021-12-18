package conditions.spring;

import conditions.business_audit_trail.repository.BusinessAuditTrailConditionRepository;
import conditions.context.UserProvider;
import conditions.core.event.EventBus;
import conditions.core.model.Country;
import conditions.core.model.Pid;
import conditions.core.repository.ConditionRepository;
import conditions.iam.model.User;
import conditions.iam.repository.IamConditionRepository;
import conditions.iam.repository.UserRepository;
import conditions.spring.repository.ConditionRepositoryImpl;
import conditions.spring.repository.RequestCachedUserRepository;
import conditions.spring.repository.SpringConditionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

@Slf4j
@Configuration
public class RepositoryConfig {

    @Bean
    public ConditionRepository conditionRepository(
            SpringConditionRepository springConditionRepository,
            EventBus eventBus,
            UserRepository userRepository,
            UserProvider userProvider,
            BusinessAuditTrailConditionRepository.BusinessAuditTrail businessAuditTrail
    ) {
        return new IamConditionRepository(
                new BusinessAuditTrailConditionRepository(
                        new ConditionRepositoryImpl(springConditionRepository, eventBus),
                        businessAuditTrail,
                        userProvider),
                userRepository,
                userProvider
        );
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
