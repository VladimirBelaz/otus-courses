package stepdefinitions;

import factory.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import listeners.DriverManager;
import org.openqa.selenium.WebDriver;

public class Hooks {

    @Before
    public void setUp(Scenario scenario) {
        // НЕ создаём драйвер
        // Он будет создан в шаге "я открываю браузер"
        System.out.println("Hook: начало сценария - " + scenario.getName());
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("Hook: драйвер закрыт");
            } catch (Exception e) {
                System.out.println("Ошибка при закрытии драйвера: " + e.getMessage());
            } finally {
                DriverManager.removeDriver();
            }
        }
        System.out.println("Hook: после сценария - " + scenario.getName());
    }
}