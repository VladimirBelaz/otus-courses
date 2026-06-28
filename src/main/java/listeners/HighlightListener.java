package listeners;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

public class HighlightListener implements WebDriverListener {

    private static final String HIGHLIGHT_SCRIPT = """
            const el = arguments[0];
            
            const oldOutline = el.style.outline;
            const oldOffset = el.style.outlineOffset;
            const oldRadius = el.style.borderRadius;
            const oldTransition = el.style.transition;
            
            el.style.transition = "outline .15s ease";
            el.style.outline = "3px solid red";
            el.style.outlineOffset = "2px";
            el.style.borderRadius = "4px";
            
            setTimeout(() => {
                el.style.outline = oldOutline;
                el.style.outlineOffset = oldOffset;
                el.style.borderRadius = oldRadius;
                el.style.transition = oldTransition;
            }, 400);
            """;

    @Override
    public void beforeClick(WebElement element) {
        highlight(element);
    }

    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        highlight(element);
    }

    private void highlight(WebElement element) {

        if (element == null) {
            return;
        }

        WebDriver driver = DriverManager.getDriver();

        if (!(driver instanceof JavascriptExecutor js)) {
            return;
        }

        try {
            js.executeScript(HIGHLIGHT_SCRIPT, element);
        } catch (WebDriverException ignored) {
        }
    }
}