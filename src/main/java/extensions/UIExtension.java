package extensions;

import com.google.inject.Guice;
import factory.DriverFactory;
import listeners.DriverManager;
import modules.PagesModule;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;

public class UIExtension implements BeforeEachCallback, AfterEachCallback {

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
        if (driver != null) {
            driver.quit();
        }
        DriverManager.removeDriver();
    }
}