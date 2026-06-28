package components;

import commons.AbsCommon;
import exceptions.CategoryNotFoundException;
import exceptions.TrainingMenuNotFoundException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.CatalogPage;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class HeaderComponent extends AbsCommon {

    private static final By TRAINING_MENU = By.cssSelector("[title='Обучение']");
    private static final By DROPDOWN = By.cssSelector(".sc-piuiz2-0");
    private static final By CATEGORY_LINKS = By.cssSelector(".sc-piuiz2-0 .sc-ig0m9y-0 a.sc-4zz0i4-0");

    private JavascriptExecutor js;
    private String selectedCategoryName;

    public HeaderComponent(WebDriver driver) {
        super(driver);
        this.js = (JavascriptExecutor) driver;
    }

    public CatalogPage openRandomCategoryFromTrainingMenu() {

        WebElement trainingMenu = findTrainingMenu();

        openTrainingDropdown(trainingMenu);

        List<WebElement> categories = getAvailableCategories();

        WebElement randomCategory = getRandomCategory(categories);

        selectedCategoryName = extractCategoryName(randomCategory);

        clickCategory(randomCategory);

        return new CatalogPage(driver);
    }

    private WebElement findTrainingMenu() {

        WebElement trainingMenu = waiters.waitForOptionalElement(TRAINING_MENU, 10);

        if (trainingMenu == null) {
            throw new TrainingMenuNotFoundException();
        }

        return trainingMenu;
    }

    private void openTrainingDropdown(WebElement trainingMenu) {

        js.executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                trainingMenu
        );

        js.executeScript(
                """
                        var event = new MouseEvent(
                            'mouseover',
                            {
                                bubbles:true,
                                cancelable:true,
                                view:window
                            }
                        );
                        arguments[0].dispatchEvent(event);
                        """,
                trainingMenu
        );

        if (waiters.waitForOptionalElement(DROPDOWN, 10) == null) {
            throw new TrainingMenuNotFoundException("Не удалось открыть меню");
        }
    }

    private List<WebElement> getAvailableCategories() {

        List<WebElement> categories =
                driver.findElements(CATEGORY_LINKS)
                        .stream()
                        .filter(this::isCourseCategory)
                        .toList();

        if (categories.isEmpty()) {
            throw new CategoryNotFoundException();
        }

        return categories;
    }

    private boolean isCourseCategory(WebElement element) {

        String href = element.getAttribute("href");

        return href != null
                && !href.contains("/spec")
                && !href.contains("/online");
    }

    private WebElement getRandomCategory(List<WebElement> categories) {

        int index = ThreadLocalRandom.current().nextInt(categories.size());

        return categories.get(index);
    }

    private String extractCategoryName(WebElement category) {
        return getCategoryName(category)
                .replaceAll("\\s*\\(\\d+\\)$", "")
                .trim();
    }

    private void clickCategory(WebElement category) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", category);
    }

    public String getSelectedCategoryName() {
        return selectedCategoryName;
    }

    private String getCategoryName(WebElement element) {
        return (String) js.executeScript(
                "return arguments[0].textContent.trim();", element);
    }
}