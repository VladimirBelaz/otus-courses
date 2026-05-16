package components;

import commons.AbsCommon;
import exceptions.ElementInteractionException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CookiePopupComponent extends AbsCommon {

    private static final By POPUP_LOCATOR = By.cssSelector(".sc-11pdrud-0");
    private static final By OK_BUTTON_LOCATOR = By.cssSelector(".sc-a6ojz8-0");

    public CookiePopupComponent(WebDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        WebElement popup = waiters.waitForOptionalElement(POPUP_LOCATOR, 1);
        return popup != null && popup.isDisplayed();
    }

    public boolean closeIfPresent() {
        try {
            if (isDisplayed()) {
                WebElement buttonElement = waiters.waitForOptionalClickable(OK_BUTTON_LOCATOR, 3);
                if (buttonElement != null) {
                    // Сразу используем JavaScript клик (обходит любые перекрытия)
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", buttonElement);
                    System.out.println("Cookie popup закрыт");
                    return true;
                }
            }
        } catch (Exception e) {
            throw new ElementInteractionException("Закрытие cookie popup", e);
        }
        return false;
    }

    public void waitAndClose() {
        WebElement popup = waiters.waitForOptionalElement(POPUP_LOCATOR, 5);
        if (popup != null && popup.isDisplayed()) {
            System.out.println("Cookie popup обнаружен, закрываем...");
            closeIfPresent();
        }
    }
}