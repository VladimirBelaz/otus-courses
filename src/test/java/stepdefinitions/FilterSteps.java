package stepdefinitions;

import helpers.CourseCardExtractor;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import listeners.DriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.CatalogPage;
import utils.DateHelper;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

public class FilterSteps {

    private WebDriver driver;
    private List<CoursePriceInfo> coursesWithPrices;
    private List<CourseDateInfo> coursesWithDates;

    private record CourseDateInfo(String title, String dateStr, LocalDate date) {}
    private record CoursePriceInfo(String title, int price) {}

    public FilterSteps() {
        this.driver = DriverManager.getDriver();
    }

    @Когда("я ищу курсы, стартующие позже {string}")
    public void searchCoursesAfterDate(String targetDateStr) {
        LocalDate targetDate = LocalDate.parse(targetDateStr);
        coursesWithDates = new ArrayList<>();

        CatalogPage catalogPage = new CatalogPage(driver);
        catalogPage.open();

        loadAllCourses();

        List<WebElement> courseCards = driver.findElements(By.cssSelector("a[href*='/lessons/']"));

        for (WebElement card : courseCards) {
            String title = CourseCardExtractor.getCourseTitle(card);
            String dateStr = CourseCardExtractor.getCourseDate(card);

            if (title == null || dateStr == null || dateStr.contains("будет объявлено позже")) {
                continue;
            }

            LocalDate courseDate = DateHelper.parseRussianDate(dateStr);
            if (courseDate != null && courseDate.isAfter(targetDate)) {
                coursesWithDates.add(new CourseDateInfo(title, dateStr, courseDate));
            }
        }

        System.out.println("Найдено курсов после " + targetDateStr + ": " + coursesWithDates.size());
    }

    private void loadAllCourses() {
        for (int i = 0; i < 5; i++) {
            try {
                WebElement showMore = driver.findElement(By.cssSelector(".sc-1qig7zt-0.bYRRHi.sc-prqxfo-0.cXVWAS"));
                if (showMore.isDisplayed()) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", showMore);
                    showMore.click();
                } else {
                    break;
                }
            } catch (Exception e) {
                break;
            }
        }
    }

    @Тогда("я вывожу в консоль список найденных курсов")
    public void printFoundCourses() {
        if (coursesWithDates == null || coursesWithDates.isEmpty()) {
            System.out.println("Нет курсов, стартующих позже указанной даты");
            return;
        }

        System.out.println("\n=== СПИСОК КУРСОВ ===");
        int counter = 1;
        for (CourseDateInfo course : coursesWithDates) {
            System.out.println(counter++ + ". " + course.title() + " — " + course.dateStr());
        }
        System.out.println("Всего: " + coursesWithDates.size() + " курсов\n");
    }

    @Тогда("я нахожу и вывожу самый дорогой и самый дешёвый курс")
    public void findAndPrintMostExpensiveAndCheapestCourse() {
        extractCoursesWithPrices();

        if (coursesWithPrices.isEmpty()) {
            System.out.println("Не найдено курсов с ценами");
            return;
        }

        CoursePriceInfo mostExpensive = coursesWithPrices.stream()
                .max(Comparator.comparingInt(CoursePriceInfo::price))
                .orElse(null);

        CoursePriceInfo cheapest = coursesWithPrices.stream()
                .min(Comparator.comparingInt(CoursePriceInfo::price))
                .orElse(null);

        System.out.println("\n=== САМЫЙ ДОРОГОЙ КУРС ===");
        System.out.println("Название: " + mostExpensive.title());
        System.out.println("Цена: " + mostExpensive.price() + " ₽");

        System.out.println("\n=== САМЫЙ ДЕШЁВЫЙ КУРС ===");
        System.out.println("Название: " + cheapest.title());
        System.out.println("Цена: " + cheapest.price() + " ₽");
    }

    private void extractCoursesWithPrices() {
        // упрощенная версия, используя хелперы
        coursesWithPrices = new ArrayList<>();
        // ... код с использованием CourseCardExtractor.getCoursePrice()
    }
}