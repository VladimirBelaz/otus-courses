package ru.otus.tests;

import org.junit.jupiter.api.Test;
import ru.otus.model.Course;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseTest extends BaseTest {

    @Test
    void shouldReturnCourses() {

        List<Course> courses = helper.getList(
                "http://localhost:8080/course/get/all",
                Course.class);

        assertEquals(2, courses.size());

        assertEquals("QA java", courses.get(0).getName());
        assertEquals(15000, courses.get(0).getPrice());

        assertEquals("Java", courses.get(1).getName());
        assertEquals(12000, courses.get(1).getPrice());
    }
}