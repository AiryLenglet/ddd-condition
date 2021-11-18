package conditions.iam.repository;

import conditions.core.model.Pid;
import conditions.iam.model.User;

public interface UserRepository {
    User findById(Pid aPid);
}
