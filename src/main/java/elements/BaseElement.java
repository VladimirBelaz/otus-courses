package elements;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import listeners.DriverManager;

import java.time.Duration;

public abstract class BaseElement {
    protected WebElement element;
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BaseElement(WebElement element) {
        this.element = element;
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected void highlight() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].style.transition = 'outline 0.15s ease-in-out';", element);
            js.executeScript("arguments[0].style.outline = '3px solid #ff4444';", element);
            js.executeScript("arguments[0].style.outlineOffset = '2px';", element);
            js.executeScript("arguments[0].style.borderRadius = '4px';", element);
            js.executeScript("setTimeout(() => arguments[0].style.outline = '', 400);", element);
        } catch (Exception ignored) {}
    }

    public boolean isDisplayed() {
        return element.isDisplayed();
    }

    public String getText() {
        highlight();
        return element.getText();
    }
}