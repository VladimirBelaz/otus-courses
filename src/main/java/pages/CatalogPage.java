package pages;

import annotations.Path;
import elements.Link;
import exceptions.CourseNotFoundException;
import exceptions.ElementInteractionException;
import jakarta.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Path("/catalog/courses")
public class CatalogPage extends AbsBasePage<CatalogPage> {

    public CatalogPage(WebDriver driver) {
        super(driver);
    }

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
                    String text = el.getText();
                    if (text != null && !text.isEmpty() && text.length() < 100
                            && !text.contains("Курс") && !text.contains("Специализация")) {
                        return text;
                    }
                }
            } catch (Exception e) {
                System.out.println("Ошибка при поиске по селектору " + selector + ": " + e.getMessage());
            }
        }
        return null;
    }

    public Optional<WebElement> findCourseByName(String courseName) {
        waiters.waitForPresence(By.cssSelector("a[href*='/lessons/']"));

        List<WebElement> cards = getCourseCards();

        System.out.println("Найдено карточек курсов: " + cards.size());

        Optional<WebElement> exactMatch = cards.stream()
                .filter(card -> {
                    String title = getCourseTitleFromCard(card);
                    return title != null && title.equalsIgnoreCase(courseName);
                })
                .findFirst();

        if (exactMatch.isPresent()) {
            System.out.println("Найдено точное совпадение: " + courseName);
            return exactMatch;
        }

        Optional<WebElement> partialMatch = cards.stream()
                .filter(card -> {
                    String title = getCourseTitleFromCard(card);
                    return title != null && title.toLowerCase().contains(courseName.toLowerCase());
                })
                .findFirst();

        if (partialMatch.isPresent()) {
            System.out.println("Найдено частичное совпадение для: " + courseName);
            return partialMatch;
        }

        return Optional.empty();
    }

    public CoursePage clickCourseByName(String courseName) {
        Optional<WebElement> courseOpt = findCourseByName(courseName);

        if (courseOpt.isPresent()) {
            WebElement courseElement = courseOpt.get();
            try {
                Link courseLink = new Link(courseElement);
                String title = courseLink.getText();

                System.out.println("Кликаем по курсу: " + title);

                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", courseElement);

                courseLink.click();

                return new CoursePage(driver);
            } catch (Exception e) {
                throw new ElementInteractionException("Клик по курсу: " + courseName, e);
            }
        }

        throw new CourseNotFoundException(courseName);
    }

    public List<WebElement> getAllCourseCards() {
        waiters.waitForPresence(By.cssSelector("a[href*='/lessons/']"));
        return getCourseCards();
    }

    public boolean isCategorySelected(String categoryName) {
        try {
            List<WebElement> labels = driver.findElements(By.cssSelector(".sc-1fry39v-1"));

            for (WebElement label : labels) {
                if (label.getText().equalsIgnoreCase(categoryName)) {
                    String forId = label.getAttribute("for");
                    if (forId != null) {
                        WebElement checkbox = driver.findElement(By.id(forId));
                        return checkbox.isSelected();
                    }
                }
            }
            return false;
        } catch (Exception e) {
            throw new ElementInteractionException("Проверка категории: " + categoryName, e);
        }
    }
}