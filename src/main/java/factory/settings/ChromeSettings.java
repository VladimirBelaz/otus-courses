package factory.settings;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;

public class ChromeSettings implements IDriverSettings {

    @Override
    public AbstractDriverOptions settings() {
        ChromeOptions options = new ChromeOptions();

        // Проверяем переменную headless (по умолчанию true)
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));

        if (headless) {
            options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--remote-allow-origins=*");
        }

        options.addArguments("--window-size=1920,1080");

        return options;
    }
}