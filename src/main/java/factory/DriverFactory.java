package factory;

import exceptions.BrowserNotSupportedException;
import factory.settings.ChromeSettings;
import factory.settings.FirefoxSettings;
import listeners.DriverManager;
import listeners.HighlightListener;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

    private static final String SELENOID_URL =
            System.getProperty("selenoid.url", "http://localhost:4444");

    private static final boolean USE_SELENOID =
            Boolean.parseBoolean(System.getProperty("use.selenoid", "true"));

    public WebDriver create() {
        return create(System.getProperty("browser", "chrome"));
    }

    public WebDriver create(String browserName) {
        WebDriver driver;
        AbstractDriverOptions options;

        switch (browserName.toLowerCase()) {
            case "chrome" -> {
                ChromeSettings chromeSettings = new ChromeSettings();
                options = chromeSettings.settings();
                options.setCapability("browserVersion", "128.0");
            }
            case "firefox" -> {
                FirefoxSettings firefoxSettings = new FirefoxSettings();
                options = firefoxSettings.settings();
                options.setCapability("browserVersion", "129.0");
            }
            default -> throw new BrowserNotSupportedException(browserName);
        }

        if (USE_SELENOID) {
            Map<String, Object> selenoidOptions = new HashMap<>();
            selenoidOptions.put("enableVNC", true);
            selenoidOptions.put("enableVideo", true);
            selenoidOptions.put("name", "UI test: " + browserName);
            selenoidOptions.put("sessionTimeout", "5m");
            options.setCapability("selenoid:options", selenoidOptions);

            driver = new RemoteWebDriver(buildUrl(SELENOID_URL + "/wd/hub"), options);
        } else {
            driver = switch (browserName.toLowerCase()) {
                case "chrome" -> new ChromeDriver((ChromeOptions) options);
                case "firefox" -> new FirefoxDriver((FirefoxOptions) options);
                default -> throw new BrowserNotSupportedException(browserName);
            };
        }

        driver.manage().window().setSize(new Dimension(1920, 1080));

        WebDriver decoratedDriver = new EventFiringDecorator(new HighlightListener()).decorate(driver);
        DriverManager.setDriver(decoratedDriver);

        return decoratedDriver;
    }

    private static URL buildUrl(String url) {
        try {
            return URI.create(url).toURL();
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Invalid Selenoid URL: " + url, e);
        }
    }
}