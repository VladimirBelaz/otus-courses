package elements;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class Link extends BaseElement {

    public Link(WebElement element) {
        super(element);
    }

    public void click() {
        highlight();
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    public String getHref() {
        highlight();
        return element.getAttribute("href");
    }

    public String getText() {
        highlight();
        return element.getText();
    }
}