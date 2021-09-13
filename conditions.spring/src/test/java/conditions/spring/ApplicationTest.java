package conditions.spring;

import conditions.core.use_case.CreateConditionUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void testHappyOpeningFulfillment() {
        final var response = this.testRestTemplate.postForEntity("/", null, CreateConditionUseCase.Response.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        final var conditionId = response.getBody().conditionId();

        assertTrue(this.testRestTemplate.postForEntity("/%s/submit".formatted(conditionId), null, Void.class).getStatusCode().is2xxSuccessful());

        final var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var body = new HttpEntity<>("{ \"action\": \"ACCEPT\"}", headers);
        assertTrue(this.testRestTemplate.exchange("/%s/approve".formatted(conditionId), HttpMethod.POST, body, Void.class).getStatusCode().is2xxSuccessful());
    }

    @Test
    void testAskingChangeAtApproval() {
        final var response = this.testRestTemplate.postForEntity("/", null, CreateConditionUseCase.Response.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        final var conditionId = response.getBody().conditionId();

        assertTrue(this.testRestTemplate.postForEntity("/%s/submit".formatted(conditionId), null, Void.class).getStatusCode().is2xxSuccessful());

        final var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var body = new HttpEntity<>("{ \"action\": \"ASK_CHANGE\", \"justification\": \"assign condition to Jim\"}", headers);
        assertTrue(this.testRestTemplate.exchange("/%s/approve".formatted(conditionId), HttpMethod.POST, body, Void.class).getStatusCode().is2xxSuccessful());

        assertTrue(this.testRestTemplate.postForEntity("/%s/submit".formatted(conditionId), null, Void.class).getStatusCode().is2xxSuccessful());

        body = new HttpEntity<>("{ \"action\": \"ACCEPT\", \"justification\": \"ok\"}", headers);
        assertTrue(this.testRestTemplate.exchange("/%s/approve".formatted(conditionId), HttpMethod.POST, body, Void.class).getStatusCode().is2xxSuccessful());
    }
}