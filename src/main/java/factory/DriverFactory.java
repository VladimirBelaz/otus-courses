package factory;

import exceptions.BrowserNotSupportedException;
import factory.settings.ChromeSettings;
import listeners.DriverManager;
import listeners.HighlightListener;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringDecorator;

public class DriverFactory {

    private String browser = System.getProperty("browser", "chrome");

    public WebDriver create() {
        WebDriver driver;

        switch (browser.toLowerCase()) {
            case "chrome" -> {
                ChromeOptions options = (ChromeOptions) new ChromeSettings().settings();
                options.addArguments("--remote-allow-origins=*");
                options.addArguments("--disable-notifications");
                driver = new ChromeDriver(options);
            }
            default -> throw new BrowserNotSupportedException(browser);
        }

        WebDriver decoratedDriver = new EventFiringDecorator(new HighlightListener()).decorate(driver);
        DriverManager.setDriver(decoratedDriver);

        driver.manage().window().maximize();

        return decoratedDriver;
    }
}