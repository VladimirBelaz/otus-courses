package helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.DateHelper;
import utils.PriceHelper;

import java.util.List;

public class CourseCardExtractor {

    public static String getCourseTitleFromCard(WebElement card) {
        String[] selectors = {
                ".sc-1yg5ro0-0", ".frUeNO", "h6",
                ".sc-1yg5ro0-1", "[class*='sc-1yg5ro0']", "h6 div"
        };

        for (String selector : selectors) {
            try {
                List<WebElement> elements = card.findElements(By.cssSelector(selector));
                for (WebElement el : elements) {
                    String text = el.getText();
                    if (text != null && !text.isEmpty() && text.length() < 100
                            && !text.contains("Курс") && !text.contains("Специализация")
                            && !text.contains("Успеть")) {
                        return text;
                    }
                }
            } catch (Exception e) {
                // продолжаем
            }
        }
        return null;
    }

    public static String getCourseDate(WebElement card) {
        try {
            String cardText = card.getText();
            return DateHelper.extractDateFromText(cardText);
        } catch (Exception e) {
            return null;
        }
    }

    public static int getCoursePrice(WebElement card) {
        String[] selectors = {
                ".sc-153sikp-11", ".sc-153sikp-11.gztHyx", ".tn-atom",
                "[class*='price']", "[class*='Price']"
        };

        for (String selector : selectors) {
            try {
                List<WebElement> elements = card.findElements(By.cssSelector(selector));
                for (WebElement el : elements) {
                    String text = el.getText();
                    if (text != null && !text.isEmpty()) {
                        int price = PriceHelper.extractNumberFromString(text);
                        if (price > 0 && price < 500000) {
                            return price;
                        }
                    }
                }
            } catch (Exception e) {
                // продолжаем
            }
        }
        return 0;
    }
}