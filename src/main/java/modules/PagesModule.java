package modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import components.CookiePopupComponent;
import components.HeaderComponent;
import org.openqa.selenium.WebDriver;
import pages.*;

public class PagesModule extends AbstractModule {

    private final WebDriver driver;

    public PagesModule(WebDriver driver) {
        this.driver = driver;
    }

    @Provides
    @Singleton
    public MainPage getMainPage() {
        return new MainPage(driver);
    }

    @Provides
    @Singleton
    public CatalogPage getCatalogPage() {
        return new CatalogPage(driver);
    }

    @Provides
    @Singleton
    public CoursePage getCoursePage() {
        return new CoursePage(driver);
    }

    @Provides
    @Singleton
    public HeaderComponent getHeaderComponent() {
        return new HeaderComponent(driver);
    }

    @Provides
    @Singleton
    public CookiePopupComponent getCookiePopupComponent() {
        return new CookiePopupComponent(driver);
    }
}