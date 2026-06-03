package factory;

import exceptions.BrowserNotSupportedException;
import factory.settings.ChromeSettings;
import factory.settings.FirefoxSettings;
import listeners.DriverManager;
import listeners.HighlightListener;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.support.events.EventFiringDecorator;

public class DriverFactory {

    // Метод без параметра - использует значение по умолчанию
    public WebDriver create() {
        return create(System.getProperty("browser", "chrome"));
    }

    // Метод с параметром - создаёт драйвер для указанного браузера
    public WebDriver create(String browserName) {
        WebDriver driver;
        AbstractDriverOptions options;

        switch (browserName.toLowerCase()) {
            case "chrome" -> {
                ChromeSettings chromeSettings = new ChromeSettings();
                options = chromeSettings.settings();
                driver = new ChromeDriver((ChromeOptions) options);
            }
            case "firefox" -> {
                FirefoxSettings firefoxSettings = new FirefoxSettings();
                options = firefoxSettings.settings();
                driver = new FirefoxDriver((FirefoxOptions) options);
            }
            default -> throw new BrowserNotSupportedException(browserName);
        }

        WebDriver decoratedDriver = new EventFiringDecorator(new HighlightListener()).decorate(driver);
        DriverManager.setDriver(decoratedDriver);

        decoratedDriver.manage().window().maximize();

        return decoratedDriver;
    }
}