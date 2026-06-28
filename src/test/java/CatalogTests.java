import extensions.UIExtension;
import helpers.CourseAnalyzer;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.CatalogPage;
import pages.CoursePage;
import pages.MainPage;

import static assertions.CourseAssertions.assertCatalogPageOpened;
import static assertions.CourseAssertions.assertCategorySelectedInFilter;
import static assertions.CourseAssertions.assertCourseExists;
import static assertions.CourseAssertions.assertCourseTitleMatches;
import static assertions.CourseAssertions.assertCoursesNotEmpty;
import static assertions.CourseAssertions.assertEarliestCourseWithJsoup;
import static assertions.CourseAssertions.assertUrlContainsCategory;

@ExtendWith(UIExtension.class)
public class CatalogTests {

    @Inject
    private MainPage mainPage;

    @Inject
    private CatalogPage catalogPage;

    @Inject
    private CourseAnalyzer analyzer;

    //СЦЕНАРИЙ 1
    @Test
    public void testFindAndOpenCourseByName() {
        String courseName = "Инженер данных";
        catalogPage.open();

        var courseOpt = catalogPage.findCourseByName(courseName);
        assertCourseExists(courseOpt.isPresent(), courseName);

        String actualTitle = catalogPage.getCourseTitleFromCard(courseOpt.get());
        CoursePage coursePage = catalogPage.clickCourseByName(courseName);
        assertCourseTitleMatches(actualTitle, coursePage.getCourseTitle());
    }

    //СЦЕНАРИЙ 2
    @Test
    public void testFindEarliestAndLatestCourseDates() {
        catalogPage.open();

        var courses = analyzer.getAllCourseInfos();
        assertCoursesNotEmpty(courses);

        var earliest = analyzer.findEarliestCourse(courses);
        var latest = analyzer.findLatestCourse(courses);

        analyzer.printResults(earliest, latest);

        CoursePage earliestPage = catalogPage.clickCourseByName(earliest.title());
        CoursePage.CourseData jsoupData = earliestPage.getCourseDataViaJsoup();

        assertEarliestCourseWithJsoup(earliest, jsoupData);
    }

    //СЦЕНАРИЙ 3
    @Test
    public void testOpenRandomCategoryFromTrainingMenu() {
        MainPage page = mainPage.open();
        CatalogPage catalogPageFromMenu = page.getHeader().openRandomCategoryFromTrainingMenu();
        String selectedCategory = page.getHeader().getSelectedCategoryName();
        String currentUrl = catalogPageFromMenu.getCurrentUrl();

        System.out.println("Выбранная категория: " + selectedCategory);
        System.out.println("URL после выбора: " + currentUrl);

        assertCatalogPageOpened(currentUrl);
        assertUrlContainsCategory(currentUrl, selectedCategory);
        assertCategorySelectedInFilter(catalogPageFromMenu.isCategorySelected(selectedCategory), selectedCategory);

        System.out.println("✅ Открыта страница: " + currentUrl);
        System.out.println("✅ Категория '" + selectedCategory + "' отмечена в фильтре");
    }
}