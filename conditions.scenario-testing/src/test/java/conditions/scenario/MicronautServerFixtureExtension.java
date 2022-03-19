package conditions.scenario;

import conditions.micronaut.Application;
import io.micronaut.runtime.Micronaut;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MicronautServerFixtureExtension implements BeforeAllCallback {

    private static final AtomicBoolean started = new AtomicBoolean(false);
    private static final Lock lock = new ReentrantLock();

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {

        lock.lock();
        if (!started.getAndSet(true)) {

            final var port = ThreadLocalRandom.current().nextInt(10000, 45000);

            final var app = Micronaut.run(Application.class, "-Dmicronaut.server.port=" + port);

            RestAssured.requestSpecification = new RequestSpecBuilder()
                    .setPort(port)
                    .build();
        }
        lock.unlock();
    }

}
