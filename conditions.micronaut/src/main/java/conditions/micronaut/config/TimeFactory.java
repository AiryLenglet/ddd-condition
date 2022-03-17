package conditions.micronaut.config;

import conditions.core.factory.Clock;
import conditions.core.factory.JavaClock;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

@Factory
public class TimeFactory {

    @Bean
    public Clock clock() {
        return new JavaClock();
    }
}
