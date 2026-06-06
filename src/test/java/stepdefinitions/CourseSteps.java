package stepdefinitions;

import io.cucumber.java.ru.*;
import listeners.DriverManager;
import org.openqa.selenium.WebDriver;
import pages.CoursePage;

public class CourseSteps {

    private CoursePage coursePage;
    private WebDriver driver;

    public CourseSteps() {
        this.driver = DriverManager.getDriver();
        this.coursePage = new CoursePage(driver);
    }


    @Когда("я перехожу в раздел {string} -> {string} -> {string}")
    public void navigateToSection(String section1, String section2, String section3) {
        driver.get("https://otus.ru/categories/online");
        System.out.println("Переход в раздел: " + section1 + " -> " + section2 + " -> " + section3);
    }

    @Тогда("заголовок страницы курса соответствует названию {string}")
    public void verifyCourseTitle(String expectedTitle) {
        String actualTitle = coursePage.getCourseTitle();
        if (!actualTitle.equals(expectedTitle)) {
            throw new AssertionError("Заголовок не совпадает: ожидалось '" + expectedTitle + "', получено '" + actualTitle + "'");
        }
        System.out.println("Заголовок страницы: " + actualTitle);
    }
}