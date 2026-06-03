package listeners;

import org.openqa.selenium.WebDriver;

public class DriverManager {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    public static void setDriver(WebDriver webDriver) {
        // Закрываем старый, если есть
        if (DRIVER.get() != null && DRIVER.get() != webDriver) {
            try {
                DRIVER.get().quit();
            } catch (Exception e) {
                // игнорируем
            }
        }
        DRIVER.set(webDriver);
    }

    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    public static void removeDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                // игнорируем
            }
        }
        DRIVER.remove();
    }
}