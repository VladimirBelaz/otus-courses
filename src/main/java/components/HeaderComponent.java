package components;

import commons.AbsCommon;
import jakarta.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import pages.CatalogPage;

import java.util.List;
import java.util.Random;

public class HeaderComponent extends AbsCommon {

    @Inject
    private CookiePopupComponent cookiePopup;

    public HeaderComponent(WebDriver driver) {
        super(driver);
    }

    public CatalogPage openRandomCategoryFromTrainingMenu() {
        cookiePopup.waitAndClose();

        WebElement trainingElement = findTrainingElement();

        if (trainingElement == null) {
            throw new RuntimeException("Элемент 'Обучение' не найден");
        }

        System.out.println("Найден элемент 'Обучение'");

        Actions actions = new Actions(driver);
        actions.moveToElement(trainingElement).perform();

        List<WebElement> categories = driver.findElements(By.cssSelector(".sc-1kjc6dh-0 .sc-4zz0i4-0"));

        List<WebElement> filteredCategories = categories.stream()
                .filter(cat -> {
                    String text = cat.getText();
                    return !text.isEmpty()
                            && !text.contains("Мои курсы")
                            && !text.contains("События")
                            && !text.contains("Другое")
                            && !text.contains("Все курсы")
                            && text.matches(".*\\d+.*");
                })
                .toList();

        if (filteredCategories.isEmpty()) {
            throw new RuntimeException("Не найдено категорий в меню 'Обучение'");
        }

        Random random = new Random();
        WebElement randomCategory = filteredCategories.get(random.nextInt(filteredCategories.size()));
        String categoryName = randomCategory.getText();

        System.out.println("Выбрана случайная категория: " + categoryName);

        randomCategory.click();

        return new CatalogPage(driver);
    }

    private WebElement findTrainingElement() {
        List<WebElement> allElements = driver.findElements(By.cssSelector("[title='Обучение']"));
        if (!allElements.isEmpty()) {
            return allElements.get(0);
        }

        return null;
    }
}