package conditions.spring.repository;

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
    public User findById(String id) {
        return this.userRepository.findById(id);
    }
}
