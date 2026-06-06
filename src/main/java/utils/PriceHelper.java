package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PriceHelper {

    public static int extractNumberFromString(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }

        Pattern pattern = Pattern.compile("(\\d+[\\s\\d]*)\\s*₽");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String priceStr = matcher.group(1).replaceAll("\\s", "");
            try {
                return Integer.parseInt(priceStr);
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        Pattern numberPattern = Pattern.compile("(\\d+[\\s\\d]*)");
        Matcher numberMatcher = numberPattern.matcher(text);
        if (numberMatcher.find()) {
            String priceStr = numberMatcher.group(1).replaceAll("\\s", "");
            try {
                int price = Integer.parseInt(priceStr);
                if (price > 1000 && price < 500000) {
                    return price;
                }
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
}