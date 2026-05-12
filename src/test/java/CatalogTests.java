import assertions.CourseAssertions;
import extensions.UIExtension;
import helpers.CourseAnalyzer;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.CatalogPage;
import pages.CoursePage;
import pages.MainPage;

import static assertions.CourseAssertions.*;

@ExtendWith(UIExtension.class)
public class CatalogTests {

    @Inject
    private MainPage mainPage;

    @Inject
    private CatalogPage catalogPage;

    private CourseAnalyzer analyzer;

    //Сценарий 1
    @Test
    public void testFindAndOpenCourseByNameSystemDesign() {
        String courseName = "System Design";

        catalogPage.open();

        var courseOpt = catalogPage.findCourseByName(courseName);

        assertCourseExists(courseOpt.isPresent(), courseName);

        String actualTitle = catalogPage.getCourseTitleFromCard(courseOpt.get());
        CoursePage coursePage = catalogPage.clickCourseByName(courseName);

        assertCourseTitleMatches(actualTitle, coursePage.getCourseTitle());
    }

    //Сценарий 2
    @Test
    public void testFindEarliestAndLatestCourseDates() {
        catalogPage.open();
        analyzer = new CourseAnalyzer(catalogPage);

        var courses = analyzer.getAllCourseInfos();
        var earliest = analyzer.findEarliestCourse(courses);
        var latest = analyzer.findLatestCourse(courses);

        CourseAssertions.assertDatesValid(analyzer.hasValidDates(courses));
        analyzer.validateEarliestWithJsoup(earliest);

        analyzer.printResults(earliest, latest);
    }

    //Сценарий 3
    @Test
    public void testOpenRandomCategoryFromTrainingMenu() {
        MainPage page = mainPage.open();

        CatalogPage catalogPageFromMenu = page.getHeader().openRandomCategoryFromTrainingMenu();
        String currentUrl = catalogPageFromMenu.getCurrentUrl();

        assertCatalogPageOpened(currentUrl);
        System.out.println("✅ Открыта страница: " + currentUrl);
    }
}