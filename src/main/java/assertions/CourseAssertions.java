package assertions;

import helpers.CourseAnalyzer;
import pages.CoursePage;
import utils.CategoryHelper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CourseAssertions {

    public static void assertCourseTitleMatches(String expectedTitle, String actualTitle) {
        assertEquals(expectedTitle, actualTitle,
                "Название курса на странице не соответствует ожидаемому");
    }

    public static void assertCourseExists(boolean found, String courseName) {
        assertTrue(found, "Курс не найден: " + courseName);
    }

    public static void assertCoursesNotEmpty(List<CourseAnalyzer.CourseInfo> courses) {
        assertFalse(courses.isEmpty(), "Нет курсов с валидными датами");
    }

    public static void assertEarliestCourseWithJsoup(CourseAnalyzer.CourseInfo earliest, CoursePage.CourseData jsoupData) {
        boolean titleMatches = jsoupData.title().toLowerCase().contains(earliest.title().toLowerCase().split("\\.")[0].trim()) ||
                earliest.title().toLowerCase().contains(jsoupData.title().toLowerCase().split("\\.")[0].trim());

        assertTrue(titleMatches,
                "Название курса '" + earliest.title() + "' не совпадает с данными через Jsoup: " + jsoupData.title());
    }


    public static void assertCatalogPageOpened(String url) {
        boolean isValid = url.contains("/catalog/courses") || url.contains("/categories/");
        assertTrue(isValid,
                "Открыта страница, не являющаяся каталогом или категорией курсов. URL: " + url);
    }

    public static void assertUrlContainsCategory(String url, String categoryName) {
        String categorySlug = CategoryHelper.getSlug(categoryName);
        boolean isValid = url.contains("categories=" + categorySlug)
                || url.endsWith("/categories/" + categorySlug + "/");
        assertTrue(isValid,
                "Страница не отфильтрована по выбранной категории '" + categoryName + "'. URL: " + url);
    }

    public static void assertCategorySelectedInFilter(boolean isSelected, String categoryName) {
        assertTrue(isSelected,
                "Категория '" + categoryName + "' не отмечена в фильтре");
    }
}