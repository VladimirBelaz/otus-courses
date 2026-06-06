package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateHelper {

    public static String extractDateFromText(String text) {
        Pattern pattern = Pattern.compile("(\\d{1,2}\\s+[а-я]+\\s*,?\\s+\\d{4})");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String dateStr = matcher.group(1);
            return dateStr.replace(",", "");
        }
        return null;
    }

    public static LocalDate parseRussianDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", new Locale("ru"));
            return LocalDate.parse(dateStr, formatter);
        } catch (Exception e) {
            try {
                String cleaned = dateStr.replace(",", "");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", new Locale("ru"));
                return LocalDate.parse(cleaned, formatter);
            } catch (Exception ex) {
                return null;
            }
        }
    }
}