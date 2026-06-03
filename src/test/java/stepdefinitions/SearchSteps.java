package stepdefinitions;

import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Тогда;
import listeners.DriverManager;
import org.openqa.selenium.WebDriver;
import pages.CatalogPage;
import pages.CoursePage;

public class SearchSteps {

    private WebDriver driver;
    private CatalogPage catalogPage;
    private CoursePage coursePage;
    private String foundCourseName;

    public SearchSteps() {
        this.driver = DriverManager.getDriver();
    }

    @И("я ищу курс с именем {string}")
    public void searchCourseByName(String courseName) {
        if (driver == null) {
            driver = DriverManager.getDriver();
        }
        catalogPage = new CatalogPage(driver);
        catalogPage.open();

        var courseOpt = catalogPage.findCourseByName(courseName);
        if (courseOpt.isEmpty()) {
            throw new AssertionError("Курс не найден: " + courseName);
        }
        foundCourseName = courseName;
        System.out.println("Курс найден: " + courseName);
    }

    @Тогда("я кликаю по найденному курсу")
    public void clickOnFoundCourse() {
        if (catalogPage == null) {
            catalogPage = new CatalogPage(driver);
        }
        coursePage = catalogPage.clickCourseByName(foundCourseName);
        System.out.println("Клик по курсу: " + foundCourseName);
    }

    @И("заголовок страницы курса соответствует названию {string}")
    public void verifyCourseTitle(String expectedTitle) {
        if (coursePage == null) {
            throw new AssertionError("Страница курса не открыта");
        }
        String actualTitle = coursePage.getCourseTitle();
        if (!actualTitle.equals(expectedTitle)) {
            throw new AssertionError("Заголовок не совпадает. Ожидалось: " + expectedTitle + ", получено: " + actualTitle);
        }
        System.out.println("Заголовок страницы соответствует: " + actualTitle);
    }
}