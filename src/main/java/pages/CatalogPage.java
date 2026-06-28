package pages;

import annotations.Path;
import elements.Link;
import exceptions.CourseNotFoundException;
import exceptions.ElementInteractionException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Path("/catalog/courses")
public class CatalogPage extends AbsBasePage<CatalogPage> {

    public CatalogPage(WebDriver driver) {
        super(driver);
    }
    private static final By COURSE_CARD = By.cssSelector("a[href*='/lessons/']");
    private static final String[] COURSE_TITLE_SELECTORS = {
            ".sc-1yg5ro0-0",
            ".frUeNO",
            "h6",
            ".sc-1yg5ro0-1",
            "[class*='sc-1yg5ro0']",
            "h6 div"
    };

    @Override
    public CatalogPage open() {
        String url = baseUrl + getPath();
        url = url.replaceAll("([^:])(/{2,})", "$1/");
        driver.get(url);
        waiters.waitForVisibility(By.cssSelector("h1"));
        return this;
    }

    private List<WebElement> getCourseCards() {
        return driver.findElements(By.cssSelector("a[href*='/lessons/']"));
    }

    public String getCourseTitleFromCard(WebElement card) {

        // Список возможных селекторов для названия курса (порядок важен)
        for (String selector : COURSE_TITLE_SELECTORS) {
            List<WebElement> elements = card.findElements(By.cssSelector(selector));
            for (WebElement el : elements) {
                String text = el.getText();
                if (isCourseTitle(text)) {
                    return text;
                }
            }
        }
        return null;
    }

    public Optional<WebElement> findCourseByName(String courseName) {
        waiters.waitForPresence(COURSE_CARD);

        List<WebElement> cards = getCourseCards();

        Optional<WebElement> partial = Optional.empty();

        for (WebElement card : cards) {

            String title = getCourseTitleFromCard(card);

            if (title == null)
                continue;

            if (title.equalsIgnoreCase(courseName))
                return Optional.of(card);

            if (partial.isEmpty()
                    && title.toLowerCase().contains(courseName.toLowerCase()))
                partial = Optional.of(card);
        }

        return partial;
    }

    public CoursePage clickCourseByName(String courseName) {
        WebElement courseElement = findCourseByName(courseName)
                .orElseThrow(() -> new CourseNotFoundException(courseName));
        try {
            Link courseLink = new Link(courseElement);

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", courseElement);

            courseLink.click();

            return new CoursePage(driver);
        } catch (WebDriverException e) {
            throw new ElementInteractionException("Клик по курсу: " + courseName);
        }
    }

    public List<WebElement> getAllCourseCards() {
        waiters.waitForPresence(COURSE_CARD);
        return getCourseCards();
    }

    private boolean isCourseTitle(String text) {
        return text != null
                && !text.isBlank()
                && text.length() < 100
                && !text.contains("Курс")
                && !text.contains("Специализация");
    }

    public boolean isCategorySelected(String categoryName) {
        try {
            // Ждем появления элемента с категорией
            WebElement label = waiters.waitForOptionalElement(
                    By.xpath(String.format(
                            "//label[contains(normalize-space(), '%s')]",
                            categoryName
                    )),5
            );

            if (label == null) {
                System.out.println("Категория '" + categoryName + "' не найдена в фильтре");
                return false;
            }

            String forId = label.getAttribute("for");
            if (forId != null) {
                WebElement checkbox = driver.findElement(By.id(forId));
                return checkbox.isSelected();
            }

            return false;
        } catch (NoSuchElementException ignored) {
            throw new ElementInteractionException("Проверка категории: " + categoryName);
        }
    }
}