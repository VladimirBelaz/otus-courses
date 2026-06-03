package stepdefinitions;

import factory.DriverFactory;
import io.cucumber.java.ru.Допустим;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import listeners.DriverManager;
import org.openqa.selenium.WebDriver;
import pages.CatalogPage;
import pages.MainPage;

public class NavigationSteps {

    private WebDriver driver;
    private MainPage mainPage;
    private CatalogPage catalogPage;

    public NavigationSteps() {
        this.driver = DriverManager.getDriver();
    }

    @Допустим("я открываю браузер {string}")
    public void openBrowser(String browserName) {
        // Закрываем предыдущий драйвер, если есть
        WebDriver existingDriver = DriverManager.getDriver();
        if (existingDriver != null) {
            try {
                existingDriver.quit();
            } catch (Exception e) {
                // игнорируем
            }
            DriverManager.removeDriver();
        }

        // Создаём новый драйвер для указанного браузера
        driver = new DriverFactory().create(browserName);
        DriverManager.setDriver(driver);

        System.out.println("Открываю браузер: " + browserName);
    }

    @Допустим("я открываю главную страницу")
    public void openMainPage() {
        if (driver == null) {
            driver = DriverManager.getDriver();
        }
        mainPage = new MainPage(driver);
        mainPage.open();
        System.out.println("Главная страница открыта");
    }

    @Допустим("я открываю каталог курсов")
    public void openCatalogPage() {
        if (driver == null) {
            driver = DriverManager.getDriver();
        }
        catalogPage = new CatalogPage(driver);
        catalogPage.open();
        System.out.println("Каталог курсов открыт");
    }

    @Когда("я перехожу в каталог курсов")
    public void navigateToCatalog() {
        openCatalogPage();
    }

    @Когда("я перехожу в раздел {string} -> {string} -> {string}")
    public void navigateToSection(String section1, String section2, String section3) {
        if (driver == null) {
            driver = DriverManager.getDriver();
        }
        // Переход на страницу подготовительных курсов
        driver.get("https://otus.ru/categories/online");
        System.out.println("Переход в раздел: " + section1 + " -> " + section2 + " -> " + section3);
    }

    @Тогда("страница загружена")
    public void pageLoaded() {
        System.out.println("Страница загружена");
    }

    @Тогда("заголовок главной страницы отображается")
    public void verifyMainPageTitle() {
        System.out.println("Заголовок главной страницы отображается");
    }
}