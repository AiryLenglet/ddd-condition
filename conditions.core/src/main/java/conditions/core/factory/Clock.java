package conditions.core.factory;

import java.time.Instant;

public interface Clock {
    Instant now();
}
