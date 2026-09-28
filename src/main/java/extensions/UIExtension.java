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
        String videoUrl = SELENOID_UI_URL + "/video/" + sessionId + ".mp4";
        try {
            // Ждём, пока видео станет доступно
            java.io.InputStream videoStream = null;
            for (int i = 0; i < 15; i++) {
                try {
                    videoStream = new java.net.URL(videoUrl).openStream();
                    break;
                } catch (java.io.FileNotFoundException e) {
                    Thread.sleep(2000);
                }
            }
            if (videoStream == null) {
                Allure.addAttachment("Video", "text/plain", "Видео не найдено: " + videoUrl);
                return;
            }
            // Прикрепляем как MP4
            Allure.addAttachment("Video", "video/mp4", videoStream, "mp4");
        } catch (Exception e) {
            Allure.addAttachment("Video Error", "text/plain", e.toString());
        }
    }
}