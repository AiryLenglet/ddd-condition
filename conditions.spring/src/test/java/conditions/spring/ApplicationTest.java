package conditions.spring;

import conditions.core.use_case.CreateConditionUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void testHappyOpeningFulfillment() {
        final var conditionId = this.testRestTemplate.postForEntity("/", null, CreateConditionUseCase.Response.class).getBody().conditionId();

        this.testRestTemplate.postForEntity("/%s/submit".formatted(conditionId), null, Void.class);

        this.testRestTemplate.postForEntity("/%s/approve".formatted(conditionId), null, Void.class);

    }
}