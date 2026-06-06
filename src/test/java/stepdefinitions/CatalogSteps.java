package stepdefinitions;

import io.cucumber.java.ru.*;
import listeners.DriverManager;
import pages.CatalogPage;

public class CatalogSteps {

    private CatalogPage catalogPage;

    public CatalogSteps() {
        this.catalogPage = new CatalogPage(DriverManager.getDriver());
    }


    @Допустим("я открываю каталог курсов")
    public void openCatalog() {
        catalogPage.open();
    }

    @Когда("я ищу курс {string}")
    public void searchCourse(String courseName) {
        var courseOpt = catalogPage.findCourseByName(courseName);
        if (courseOpt.isEmpty()) {
            throw new AssertionError("Курс не найден: " + courseName);
        }
        System.out.println("Курс найден: " + courseName);
    }

    @Тогда("я вижу корректную страницу курса")
    public void checkCoursePage() {
        // передаём управление CourseSteps
    }
}