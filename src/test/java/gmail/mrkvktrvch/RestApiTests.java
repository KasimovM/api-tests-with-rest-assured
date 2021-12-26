package gmail.mrkvktrvch;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class RestApiTests {
    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://reqres.in/";
    }

    @Test
    void createUserTest() {
         String data = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/users/")
                .then()
                .statusCode(201)
                .body("name", is("morpheus"), "job", is("leader"));
    }

    @Test
    void singleUserGetTest() {
        given()
                .when()
                .get("/api/users/2")
                .then()
                .statusCode(200)
                .body("data.first_name", is("Janet"), "data.last_name", is("Weaver"));
    }

    @Test
    void successfulRegistrationTest() {
        String data = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/register/")
                .then()
                .statusCode(200)
                .body("id", is(4), "token", is("QpwL5tke4Pnpja7X4"));

    }

    @Test
    void deleteTest() {

        given()
                .when()
                .delete("/api/users/2")
                .then()
                .statusCode(204);
    }

    @Test
    void unsuccessfulRegisterTest() {
        String data = "{ \"email\": \"sydney@fife\"}";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/register/")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }
}
