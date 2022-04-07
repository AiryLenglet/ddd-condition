package conditions.scenario;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import conditions.micronaut.Application;
import io.micronaut.runtime.Micronaut;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.opentest4j.AssertionFailedError;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public class MicronautServerFixtureExtension implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback {

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

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        final var appender = new ListAppender<ILoggingEvent>();
        appender.start();
        final var logger = (Logger) LoggerFactory.getLogger(ROOT_LOGGER_NAME);
        logger.addAppender(appender);

        getStore(context).put("log-appender", appender);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        final var appender = (ListAppender<ILoggingEvent>) getStore(context).remove("log-appender");
        appender.stop();
        checkSensitiveDataLeakage(appender);
    }

    private void checkSensitiveDataLeakage(ListAppender<ILoggingEvent> appender) throws IOException {
        final var blacklistedWords = Set.of(Files.readString(Path.of("src/test/resources/sensitiveData")).split("\n"));
        final var leakingLog = appender.list.stream()
                .map(ILoggingEvent::getFormattedMessage)
                .filter(m -> blacklistedWords.stream().anyMatch(m::contains))
                .findFirst();
        if (leakingLog.isPresent()) {
            throw new AssertionFailedError("log '" + leakingLog.get() + "' leak sensitive data");
        }
    }

    private ExtensionContext.Store getStore(ExtensionContext context) {
        return context.getStore(ExtensionContext.Namespace.create(getClass(), context.getRequiredTestMethod()));
    }

}
