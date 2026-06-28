package factory.settings;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;

public class FirefoxSettings implements IDriverSettings {

    @Override
    public AbstractDriverOptions settings() {
        FirefoxOptions options = new FirefoxOptions();

        // Добавляем headless для Firefox
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));

        if (headless) {
            options.addArguments("--headless");
        }

        options.addArguments("--width=1920", "--height=1080");
        options.addPreference("dom.webnotifications.enabled", false);
        options.addPreference("browser.privatebrowsing.autostart", true);
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        return options;
    }
}