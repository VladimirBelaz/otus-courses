package listeners;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

public class HighlightListener implements WebDriverListener {

    @Override
    public void beforeClick(WebElement element) {
        highlight(element);
    }

    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        highlight(element);
    }

    private void highlight(WebElement element) {
        try {
            var js = (org.openqa.selenium.JavascriptExecutor) DriverManager.getDriver();
            js.executeScript("arguments[0].style.transition = 'outline 0.15s ease-in-out';", element);
            js.executeScript("arguments[0].style.outline = '3px solid #ff4444';", element);
            js.executeScript("arguments[0].style.outlineOffset = '2px';", element);
            js.executeScript("arguments[0].style.borderRadius = '4px';", element);
            js.executeScript("setTimeout(() => arguments[0].style.outline = '', 400);", element);
        } catch (Exception ignored) {
        }
    }
}