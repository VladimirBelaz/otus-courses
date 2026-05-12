package pages;

import annotations.Path;
import components.CookiePopupComponent;
import elements.Link;
import elements.TextBlock;
import jakarta.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Optional;

@Path("/catalog/courses")
public class CatalogPage extends AbsBasePage<CatalogPage> {

    @Inject
    private CookiePopupComponent cookiePopup;

    public CatalogPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public CatalogPage open() {
        String url = baseUrl + getPath();
        driver.get(url);
        cookiePopup.waitAndClose();
        waiters.waitForPresence(By.cssSelector("a[href*='/lessons/']"));
        return this;
    }

    private List<WebElement> getCourseCards() {
        return driver.findElements(By.cssSelector("a[href*='/lessons/']"));
    }

    public String getCourseTitleFromCard(WebElement card) {
        String[] selectors = {
                ".sc-1yg5ro0-0",
                ".frUeNO",
                "h6",
                ".sc-1yg5ro0-1",
                "[class*='sc-1yg5ro0']",
                "h6 div"
        };

        for (String selector : selectors) {
            try {
                List<WebElement> elements = card.findElements(By.cssSelector(selector));
                for (WebElement el : elements) {
                    TextBlock textBlock = new TextBlock(el);
                    String text = textBlock.getText();
                    if (text != null && !text.isEmpty() && text.length() < 100 && !text.contains("Курс") && !text.contains("Специализация")) {
                        return text;
                    }
                }
            } catch (Exception ignored) {}
        }
        return "";
    }

    public Optional<WebElement> findCourseByName(String courseName) {
        waiters.waitForPresence(By.cssSelector("a[href*='/lessons/']"));

        List<WebElement> cards = getCourseCards();

        System.out.println("Найдено карточек курсов: " + cards.size());

        Optional<WebElement> exactMatch = cards.stream()
                .filter(card -> {
                    String title = getCourseTitleFromCard(card);
                    System.out.println("Название курса: " + title);
                    return title.equalsIgnoreCase(courseName);
                })
                .findFirst();

        if (exactMatch.isPresent()) {
            System.out.println("Найдено точное совпадение: " + courseName);
            return exactMatch;
        }

        Optional<WebElement> partialMatch = cards.stream()
                .filter(card -> {
                    String title = getCourseTitleFromCard(card);
                    return title.toLowerCase().contains(courseName.toLowerCase());
                })
                .findFirst();

        if (partialMatch.isPresent()) {
            System.out.println("Найдено частичное совпадение для: " + courseName);
            return partialMatch;
        }

        return Optional.empty();
    }

    public CoursePage clickCourseByName(String courseName) {
        cookiePopup.closeIfPresent();

        Optional<WebElement> courseOpt = findCourseByName(courseName);

        if (courseOpt.isPresent()) {
            WebElement courseElement = courseOpt.get();
            Link courseLink = new Link(courseElement);
            String title = courseLink.getText();

            System.out.println("Кликаем по курсу: " + title);

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", courseElement);

            courseLink.click();

            return new CoursePage(driver);
        }

        throw new RuntimeException("Курс с названием '" + courseName + "' не найден в каталоге");
    }

    public List<WebElement> getAllCourseCards() {
        waiters.waitForPresence(By.cssSelector("a[href*='/lessons/']"));
        return getCourseCards();
    }
}