package stepdefinitions;

import factory.DriverFactory;
import io.cucumber.java.ru.*;
import listeners.DriverManager;
import org.openqa.selenium.WebDriver;

public class BrowserSteps {

    private WebDriver driver;

    @Допустим("я открываю браузер {string}")
    public void openBrowser(String browserName) {
        System.setProperty("browser", browserName);
        driver = new DriverFactory().create();
        DriverManager.setDriver(driver);
        System.out.println("Браузер " + browserName + " открыт");
    }

    @Допустим("я открываю главную страницу")
    public void openMainPage() {
        driver.get("https://otus.ru");
        System.out.println("Главная страница открыта");
    }

    @Тогда("заголовок главной страницы отображается")
    public void verifyMainPageTitle() {
        String title = driver.getTitle();
        System.out.println("Заголовок: " + title);

        if (title == null || title.isEmpty()) {
            throw new AssertionError("Заголовок страницы не отображается");
        }
    }
}