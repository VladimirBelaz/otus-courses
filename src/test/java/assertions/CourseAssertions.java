package assertions;

import pages.CoursePage;
import helpers.CourseAnalyzer;

import static org.junit.jupiter.api.Assertions.*;

public class CourseAssertions {

    public static void assertCourseTitleMatches(String expectedTitle, String actualTitle) {
        assertEquals(expectedTitle, actualTitle,
                "Название курса на странице не соответствует ожидаемому");
    }

    public static void assertCoursePageOpened(String url) {
        assertTrue(url.contains("/lessons/"),
                "Открыта страница, не являющаяся страницей курса. URL: " + url);
    }

    public static void assertCatalogPageOpened(String url) {
        assertTrue(url.contains("/catalog/courses") || url.contains("/categories/"),
                "Открыта страница, не являющаяся каталогом курсов. URL: " + url);
    }

    public static void assertCourseExists(boolean found, String courseName) {
        assertTrue(found, "Курс не найден: " + courseName);
    }

    public static void assertCoursesNotEmpty(boolean isEmpty, String message) {
        assertFalse(isEmpty, message);
    }

    public static void assertEarliestCourseWithJsoup(CourseAnalyzer.CourseInfo earliest, CoursePage.CourseData jsoupData) {
        boolean titleMatches = jsoupData.title().toLowerCase().contains(earliest.title().toLowerCase().split("\\.")[0].trim()) ||
                earliest.title().toLowerCase().contains(jsoupData.title().toLowerCase().split("\\.")[0].trim());

        assertTrue(titleMatches,
                "Название курса '" + earliest.title() + "' не совпадает с данными через Jsoup: " + jsoupData.title());
    }

    public static void assertDatesValid(boolean hasValidDates) {
        assertTrue(hasValidDates, "Нет курсов с валидными датами");
    }
}