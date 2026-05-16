package components;

import commons.AbsCommon;
import elements.Button;
import org.openqa.selenium.By;
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
                    Button okButton = new Button(buttonElement);
                    okButton.click();
                    System.out.println("Cookie popup закрыт");
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка при закрытии cookie popup: " + e.getMessage());
        }
        return false;
    }

    public void waitAndClose() {
        WebElement popup = waiters.waitForOptionalElement(POPUP_LOCATOR, 1);  // ← 1 секунда вместо 5

        if (popup != null && popup.isDisplayed()) {
            System.out.println("Cookie popup обнаружен, закрываем...");
            closeIfPresent();
        }
    }
}