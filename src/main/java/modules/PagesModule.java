package modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import components.HeaderComponent;
import helpers.CourseAnalyzer;
import org.openqa.selenium.WebDriver;
import pages.CatalogPage;
import pages.CoursePage;
import pages.MainPage;

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
    public CourseAnalyzer getCourseAnalyzer(CatalogPage catalogPage) {
        return new CourseAnalyzer(catalogPage);
    }

    @Provides
    @Singleton
    public HeaderComponent getHeaderComponent() {
        return new HeaderComponent(driver);
    }
}