package conditions.iam.repository;

import conditions.iam.model.User;

public interface UserRepository {
    User findById(String id);
}
