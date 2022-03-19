package conditions.scenario;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

import static io.restassured.RestAssured.given;

@ExtendWith({MicronautServerFixtureExtension.class})
class DiscardConditionTest {

    @Test
    @DisplayName("Discard condition scenario")
    void test() throws IOException, InterruptedException {


        final var conditionId = given()
                .contentType(ContentType.JSON)
                .when()
                .post("/conditions")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        given()
                .contentType(ContentType.JSON)
                .when()
                .put("/conditions/" + conditionId + "/discard")
                .then()
                .statusCode(200);
    }
}
