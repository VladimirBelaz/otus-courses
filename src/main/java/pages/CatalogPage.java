package pages;

import annotations.Path;
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

    // Только карточки КАТАЛОГА, без рекомендаций из шапки и слайдера внизу.
    // "styles__Courses" — стабильное имя styled-components-контейнера,
    // в отличие от хеша sc-18q05a6-1.
    private static final By COURSE_CARD =
            By.cssSelector("div[class*='styles__Courses'] a[href^='/lessons/']");

    // h6 — единственный селектор, который реально совпадает в текущей вёрстке.
    // Второй — на случай, если h6 обернут по-другому после редизайна.
    private static final String[] COURSE_TITLE_SELECTORS = {
            "h6",
            "h6 div"
    };

    @Override
    public CatalogPage open() {
        String url = baseUrl + getPath();
        driver.get(url);
        waiters.waitForVisibility(By.cssSelector("h1"));
        return this;
    }

    private List<WebElement> getCourseCards() {
        return driver.findElements(COURSE_CARD);
    }

    public String getCourseTitleFromCard(WebElement card) {
        for (String selector : COURSE_TITLE_SELECTORS) {
            for (WebElement element : card.findElements(By.cssSelector(selector))) {
                String text = element.getText();
                if (isCourseTitle(text)) {
                    return text.trim();
                }
            }
        }
        return null;
    }

    public Optional<WebElement> findCourseByName(String courseName) {
        waiters.waitForPresence(COURSE_CARD);

        List<WebElement> cards = getCourseCards();
        Optional<WebElement> partial = Optional.empty();

        String expected = courseName.toLowerCase();
        for (WebElement card : cards) {
            String title = getCourseTitleFromCard(card);
            if (title == null) continue;

            if (title.equalsIgnoreCase(courseName)) {
                return Optional.of(card);
            }
            if (partial.isEmpty() && title.toLowerCase().contains(expected)) {
                partial = Optional.of(card);
            }
        }
        return partial;
    }

    public CoursePage clickCourseByName(String courseName) {
        WebElement course = findCourseByName(courseName)
                .orElseThrow(() -> new CourseNotFoundException(courseName));

        try {
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({block:'center'});", course);
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", course);

            waiters.waitForVisibility(By.cssSelector("h1"));
            return new CoursePage(driver);
        } catch (WebDriverException e) {
            throw new ElementInteractionException(
                    "Не удалось открыть курс: " + courseName, e);
        }
    }

    public List<WebElement> getAllCourseCards() {
        waiters.waitForPresence(COURSE_CARD);
        return getCourseCards();
    }

    private boolean isCourseTitle(String text) {
        if (text == null) return false;
        String t = text.trim();
        return !t.isBlank()
                && !t.contains("Курс")
                && !t.contains("Специализация")
                && t.length() < 100;
    }

    public boolean isCategorySelected(String categoryName) {
        try {
            WebElement label = waiters.waitForOptionalElement(
                    By.xpath(String.format(
                            "//label[contains(normalize-space(), '%s')]", categoryName)),
                    5);

            if (label == null) {
                System.out.println("Категория '" + categoryName + "' не найдена в фильтре");
                return false;
            }

            String forId = label.getAttribute("for");
            if (forId != null && !forId.isBlank()) {
                WebElement checkbox = driver.findElement(By.id(forId));
                return checkbox.isSelected();
            }
            return false;
        } catch (NoSuchElementException ignored) {
            throw new ElementInteractionException("Проверка категории: " + categoryName);
        }
    }
}