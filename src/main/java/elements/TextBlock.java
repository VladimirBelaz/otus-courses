package elements;

import org.openqa.selenium.WebElement;

public class TextBlock extends BaseElement {

    public TextBlock(WebElement element) {
        super(element);
    }

    public String getText() {
        highlight();
        return element.getText();
    }

    public boolean containsText(String text) {
        highlight();
        return element.getText().contains(text);
    }
}