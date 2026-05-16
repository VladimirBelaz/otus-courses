package elements;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class Button extends BaseElement {

    public Button(WebElement element) {
        super(element);
    }

    public void click() {
        highlight();  
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    public boolean isEnabled() {
        return element.isEnabled();
    }

    public String getText() {
        highlight();
        return element.getText();
    }
}