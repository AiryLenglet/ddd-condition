package conditions.core.factory;

import java.time.Instant;

public class JavaClock implements Clock {
    @Override
    public Instant now() {
        return Instant.now();
    }
}
