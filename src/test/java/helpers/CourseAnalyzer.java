// src/test/java/helpers/CourseAnalyzer.java
package helpers;

import assertions.CourseAssertions;
import pages.CatalogPage;
import pages.CoursePage;
import utils.DateUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.util.List;

public class CourseAnalyzer {

    private final CatalogPage catalogPage;

    public CourseAnalyzer(CatalogPage catalogPage) {
        this.catalogPage = catalogPage;
    }

    public record CourseInfo(String title, String dateStr, LocalDate date) {}

    public List<CourseInfo> getAllCourseInfos() {
        List<WebElement> courseCards = catalogPage.getAllCourseCards();
        return courseCards.stream()
                .map(this::extractCourseInfo)
                .filter(info -> info != null && info.date != LocalDate.MAX)
                .toList();
    }

    private CourseInfo extractCourseInfo(WebElement card) {
        try {
            String title = catalogPage.getCourseTitleFromCard(card);
            if (title == null || title.isEmpty()) return null;

            String dateStr = extractDateString(card);
            LocalDate date = DateUtils.parseRussianDate(dateStr);

            return new CourseInfo(title, dateStr, date);
        } catch (Exception e) {
            return null;
        }
    }

    private String extractDateString(WebElement card) {
        try {
            return card.findElement(By.cssSelector(".sc-157icee-1")).getText();
        } catch (Exception e) {
            return "Дата не указана";
        }
    }

    public CourseInfo findEarliestCourse(List<CourseInfo> courses) {
        return courses.stream()
                .reduce((a, b) -> a.date.isBefore(b.date) ? a : b)
                .orElseThrow(() -> new AssertionError("Нет курсов для сравнения"));
    }

    public CourseInfo findLatestCourse(List<CourseInfo> courses) {
        return courses.stream()
                .reduce((a, b) -> a.date.isAfter(b.date) ? a : b)
                .orElseThrow(() -> new AssertionError("Нет курсов для сравнения"));
    }

    public void printResults(CourseInfo earliest, CourseInfo latest) {
        System.out.println("=== РЕЗУЛЬТАТЫ ===");
        System.out.println("Самый ранний курс: " + earliest.title() + " — " + earliest.dateStr());
        System.out.println("Самый поздний курс: " + latest.title() + " — " + latest.dateStr());
    }

    public void validateEarliestWithJsoup(CourseInfo earliest) {
        CoursePage earliestPage = catalogPage.clickCourseByName(earliest.title());
        CoursePage.CourseData jsoupData = earliestPage.getCourseDataViaJsoup();

        CourseAssertions.assertEarliestCourseWithJsoup(earliest, jsoupData);
    }

    public boolean hasValidDates(List<CourseInfo> courses) {
        return !courses.isEmpty();
    }
}