package ru.otus.tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

class SchemaValidationTest extends BaseTest {

    @Test
    void shouldValidateScoreSchema() {
        String response = helper.get("http://localhost:8080/user/get/1");
        given()
                .body(response)
                .then()
                .body(matchesJsonSchemaInClasspath("schemas/score-schema.json"));
    }

    @Test
    void shouldValidateUsersSchema() {
        String response = helper.get("http://localhost:8080/user/get/all");
        given()
                .body(response)
                .then()
                .body(matchesJsonSchemaInClasspath("schemas/users-schema.json"));
    }

    @Test
    void shouldValidateCoursesSchema() {
        String response = helper.get("http://localhost:8080/course/get/all");
        given()
                .body(response)
                .then()
                .body(matchesJsonSchemaInClasspath("schemas/course-schema.json"));
    }
}