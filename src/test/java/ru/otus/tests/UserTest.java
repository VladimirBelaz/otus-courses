package ru.otus.tests;

import org.junit.jupiter.api.Test;
import ru.otus.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest extends BaseTest {

    @Test
    void shouldReturnUsers() {

        List<User> users = helper.getList(
                "http://localhost:8080/user/get/all",
                User.class);

        assertEquals(1, users.size());

        User user = users.get(0);

        assertEquals("Test user", user.getName());
        assertEquals("QA", user.getCourse());
        assertEquals("test@test.test", user.getEmail());
        assertEquals(23, user.getAge());
    }
}