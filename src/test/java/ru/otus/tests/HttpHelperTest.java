package ru.otus.tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpHelperTest extends BaseTest {

    @Test
    void shouldReturnUserScore() {
        String response = helper.get("http://localhost:8080/user/get/1");
        System.out.println(response);
        assertTrue(response.contains("Test user"));
        assertTrue(response.contains("78"));
    }
}