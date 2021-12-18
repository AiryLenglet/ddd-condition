package conditions.context;

import conditions.core.model.Pid;

public interface UserProvider {
    Pid currentUser();
}
