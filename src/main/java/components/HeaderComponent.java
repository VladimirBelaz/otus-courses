package components;

import commons.AbsCommon;
import elements.Link;
import exceptions.CategoryNotFoundException;
import exceptions.ElementInteractionException;
import exceptions.TrainingMenuNotFoundException;
import jakarta.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import pages.CatalogPage;

import java.util.List;
import java.util.Random;

public class HeaderComponent extends AbsCommon {

    private String selectedCategoryName;

    public HeaderComponent(WebDriver driver) {
        super(driver);
    }

    public CatalogPage openRandomCategoryFromTrainingMenu() {

        WebElement trainingElement = findTrainingElement();

        if (trainingElement == null) {
            throw new TrainingMenuNotFoundException();
        }

        System.out.println("Найден элемент 'Обучение'");

        Actions actions = new Actions(driver);
        actions.moveToElement(trainingElement).perform();

        waiters.waitForVisibility(By.cssSelector(".sc-piuiz2-0"));

        List<WebElement> categoryLinks = driver.findElements(By.cssSelector(".sc-piuiz2-0 a[href*='/categories/']"));

        List<WebElement> filteredCategories = categoryLinks.stream()
                .filter(link -> {
                    String href = link.getAttribute("href");
                    String text = link.getText();
                    return !href.contains("/spec")
                            && !href.contains("/online")
                            && text != null
                            && !text.isEmpty()
                            && text.matches(".*\\d+.*");
                })
                .toList();

        if (filteredCategories.isEmpty()) {
            throw new CategoryNotFoundException();
        }

        Random random = new Random();
        WebElement randomCategoryElement = filteredCategories.get(random.nextInt(filteredCategories.size()));

        String fullText = randomCategoryElement.getText();
        selectedCategoryName = fullText.replaceAll("\\s*\\(\\d+\\)$", "").trim();

        System.out.println("Выбрана случайная категория: " + selectedCategoryName);

        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", randomCategoryElement);

            Link categoryLink = new Link(randomCategoryElement);
            categoryLink.click();
        } catch (Exception e) {
            throw new ElementInteractionException("Клик по категории: " + selectedCategoryName, e);
        }

        return new CatalogPage(driver);
    }

    public String getSelectedCategoryName() {
        return selectedCategoryName;
    }

    private WebElement findTrainingElement() {
        try {
            waiters.waitForVisibility(By.cssSelector(".sc-1youhxc-1"));
        } catch (Exception e) {
            // Элемент может появиться позже, продолжаем поиск
        }

        List<WebElement> elements = driver.findElements(By.cssSelector(".sc-1youhxc-1"));
        for (WebElement el : elements) {
            if (el.getText().equals("Обучение")) {
                return el;
            }
        }

        List<WebElement> allElements = driver.findElements(By.cssSelector("[title='Обучение']"));
        if (!allElements.isEmpty()) {
            return allElements.get(0);
        }

        return null;
    }
}