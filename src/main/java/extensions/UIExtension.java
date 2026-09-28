package extensions;

import com.google.inject.Guice;
import factory.DriverFactory;
import io.qameta.allure.Allure;
import listeners.DriverManager;
import modules.PagesModule;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;

public class UIExtension implements BeforeEachCallback, AfterEachCallback {

    private static final String SELENOID_UI_URL =
            System.getProperty("selenoid.ui.url", "http://localhost:8081");

    private WebDriver driver;

    @Override
    public void beforeEach(ExtensionContext context) {
        driver = new DriverFactory().create();
        DriverManager.setDriver(driver);

        Guice.createInjector(new PagesModule(driver))
                .injectMembers(context.getRequiredTestInstance());
    }

    @Override
    public void afterEach(ExtensionContext context) {
        String sessionId = DriverManager.getSessionId();

        if (driver != null) {
            driver.quit();
        }
        DriverManager.removeDriver();

        if (sessionId != null) {
            attachVideo(sessionId);
        }
    }

    private void attachVideo(String sessionId) {
        String videoHtml = "<html><body>"
                + "<video controls width='100%'>"
                + "<source src='" + SELENOID_UI_URL + "/video/" + sessionId + ".mp4' type='video/mp4'>"
                + "</video>"
                + "</body></html>";
        Allure.addAttachment("Video", "text/html", videoHtml, ".html");
    }
}