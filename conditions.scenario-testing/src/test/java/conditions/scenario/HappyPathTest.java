package conditions.scenario;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

import static io.restassured.RestAssured.given;

@ExtendWith({MicronautServerFixtureExtension.class})
class HappyPathTest {

    @DisplayName("Happy path scenario")
    @Test
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
                .put("/conditions/" + conditionId + "/start")
                .then()
                .statusCode(200);

        final var currentTaskId = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/conditions/" + conditionId)
                .then()
                .statusCode(200)
                .extract()
                .path("openTask.id");

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "comment": "that's great"
                        }
                        """)
                .when()
                .put("/tasks/" + currentTaskId + "/complete")
                .then()
                .statusCode(200);
    }
}
