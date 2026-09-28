package listeners;

import org.openqa.selenium.WebDriver;

public class DriverManager {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private static final ThreadLocal<String> SESSION_ID = new ThreadLocal<>();

    public static void setDriver(WebDriver webDriver) {
        DRIVER.set(webDriver);
    }

    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    public static void removeDriver() {
        DRIVER.remove();
        SESSION_ID.remove();
    }

    public static void setSessionId(String sessionId) {
        SESSION_ID.set(sessionId);
    }

    public static String getSessionId() {
        return SESSION_ID.get();
    }
}