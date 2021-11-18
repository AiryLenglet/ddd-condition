package conditions.spring.repository;

import conditions.core.model.Pid;
import conditions.iam.model.User;
import conditions.iam.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;

public class RequestCachedUserRepository implements UserRepository {

    private final UserRepository userRepository;

    public RequestCachedUserRepository(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Cacheable(cacheManager = "requestScopedCacheManager", cacheNames = "default")
    @Override
    public User findById(Pid aPid) {
        return this.userRepository.findById(aPid);
    }
}