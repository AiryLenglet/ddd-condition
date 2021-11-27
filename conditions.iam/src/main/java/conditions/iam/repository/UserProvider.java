package conditions.iam.repository;

import conditions.core.model.Pid;

public interface UserProvider {
    Pid currentUser();
}
