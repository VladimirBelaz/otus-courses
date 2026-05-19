package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Waiters {

    private WebDriver driver;
    private WebDriverWait wait;

    public Waiters(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public WebElement waitForOptionalElement(By locator, int timeoutSeconds) {
        try {
            WebDriverWait optionalWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return optionalWait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (TimeoutException e) {
            return null;
        }
    }

    public WebElement waitForOptionalClickable(By locator, int timeoutSeconds) {
        try {
            WebDriverWait optionalWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return optionalWait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            return null;
        }
    }
}