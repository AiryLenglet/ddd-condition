package conditions.scenario;

import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

import static io.restassured.RestAssured.given;

@ExtendWith({MicronautServerFixtureExtension.class})
class AskForChangeThenAcceptPathTest {

    @DisplayName("Ask for a change then accept path scenario")
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

        /* CONDITION SETUP */
        var currentTaskId = getCurrentTaskId(conditionId);
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

        /* APPROVAL */
        currentTaskId = getCurrentTaskId(conditionId);
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "comment": "change something please",
                            "decision": "ASK_FOR_CHANGE"
                        }
                        """)
                .when()
                .put("/tasks/" + currentTaskId + "/complete")
                .then()
                .statusCode(200);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/conditions/" + conditionId)
                .then()
                .statusCode(200)
                .body("status", Matchers.is("DRAFT"));

        /* CONDITION SETUP */
        currentTaskId = getCurrentTaskId(conditionId);
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

        /* APPROVAL */
        currentTaskId = getCurrentTaskId(conditionId);
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "comment": "ok now",
                            "decision": "ACCEPT"
                        }
                        """)
                .when()
                .put("/tasks/" + currentTaskId + "/complete")
                .then()
                .statusCode(200);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/conditions/" + conditionId)
                .then()
                .statusCode(200)
                .body("status", Matchers.is("OPEN"));
    }

    private String getCurrentTaskId(Object conditionId) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get("/conditions/" + conditionId)
                .then()
                .statusCode(200)
                .extract()
                .path("openTask.id");
    }
}
