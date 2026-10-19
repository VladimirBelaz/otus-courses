package components;

import commons.AbsCommon;
import exceptions.CategoryNotFoundException;
import exceptions.TrainingMenuNotFoundException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import pages.CatalogPage;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class HeaderComponent extends AbsCommon {

    // title задан вручную в вёрстке — стабильный якорь
    private static final By TRAINING_MENU = By.cssSelector("[title='Обучение']");

    // Признак ОТКРЫТОГО дропдауна "Обучение":
    // ссылка на /categories/programming есть только в нём (в "Информация",
    // "Компаниям" и т.п. — нет). visibilityOf ждёт реального открытия.
    private static final By TRAINING_DROPDOWN_READY =
            By.cssSelector("[class*='piuiz2-0'] a[href*='/categories/programming']");

    // Все ссылки направлений — только внутри дропдауна (защита от глобального поиска)
    private static final By CATEGORY_LINKS =
            By.cssSelector("[class*='piuiz2-0'] a[href*='/categories/']");

    private final JavascriptExecutor js;
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
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", trainingMenu);

        // 1) Реальный hover через Actions — на React-дропдаунах это работает надёжнее
        new Actions(driver)
                .moveToElement(trainingMenu)
                .pause(Duration.ofMillis(300))
                .perform();

        // 2) Fallback: синтетические события (mouseover + mouseenter + mousemove),
        //    если сайт слушает их, а не настоящие события браузера
        js.executeScript("""
                var el = arguments[0];
                ['mouseover','mouseenter','mousemove'].forEach(function(name){
                    el.dispatchEvent(new MouseEvent(name, {
                        bubbles: name !== 'mouseenter',
                        cancelable: true,
                        view: window
                    }));
                });
                """, trainingMenu);

        // 3) Ждём ВИДИМОСТЬ ссылки внутри дропдауна, а не просто presence
        try {
            waiters.waitForVisibility(TRAINING_DROPDOWN_READY);
        } catch (Exception e) {
            throw new TrainingMenuNotFoundException("Не удалось открыть меню");
        }
    }

    private List<WebElement> getAvailableCategories() {
        List<WebElement> categories = driver.findElements(CATEGORY_LINKS)
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
        js.executeScript("arguments[0].click();", category);
    }

    public String getSelectedCategoryName() {
        return selectedCategoryName;
    }

    private String getCategoryName(WebElement element) {
        return (String) js.executeScript(
                "return arguments[0].textContent.trim();", element);
    }
}