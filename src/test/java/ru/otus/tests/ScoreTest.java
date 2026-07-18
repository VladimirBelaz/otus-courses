package ru.otus.tests;

import org.junit.jupiter.api.Test;
import ru.otus.model.Score;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoreTest extends BaseTest {

    @Test
    void shouldDeserializeScore() {
        Score score = helper.get("http://localhost:8080/user/get/1", Score.class);
        assertEquals("Test user", score.getName());
        assertEquals(78, score.getScore());
    }
}