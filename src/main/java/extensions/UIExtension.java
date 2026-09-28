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

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

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
        URL url;
        try {
            url = URI.create(videoUrl).toURL();
        } catch (Exception e) {
            Allure.addAttachment("Video Error", "text/plain",
                    "Invalid URL: " + videoUrl + "\n" + e);
            return;
        }

        // Ждём, пока Selenoid допишет файл (до 30 секунд)
        InputStream videoStream = null;
        for (int attempt = 1; attempt <= 15; attempt++) {
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("HEAD");
                int code = connection.getResponseCode();
                if (code == 200) {
                    long size1 = connection.getContentLengthLong();
                    Thread.sleep(2000);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("HEAD");
                    long size2 = connection.getContentLengthLong();
                    if (size1 == size2 && size1 > 0) {
                        videoStream = url.openStream();
                        break;
                    }
                }
            } catch (Exception ignored) {
                // пробуем ещё раз
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        if (videoStream != null) {
            Allure.addAttachment("Video", "video/mp4", videoStream, "mp4");
        } else {
            Allure.addAttachment("Video unavailable", "text/plain",
                    "Не удалось скачать видео за 30 секунд: " + videoUrl
                            + "\nПроверь, что Selenoid UI доступен по этому адресу.");
        }
    }
}