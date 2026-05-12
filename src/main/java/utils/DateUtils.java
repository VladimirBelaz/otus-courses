package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

public class DateUtils {

    private static final DateTimeFormatter RUSSIAN_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("dd MMMM, yyyy")
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .toFormatter(new Locale("ru"));

    public static LocalDate parseRussianDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty() || dateStr.contains("Август") && !dateStr.contains(",")) {
            return LocalDate.MAX;
        }
        try {
            String cleanDate = dateStr.split("·")[0].trim();
            return LocalDate.parse(cleanDate, RUSSIAN_FORMATTER);
        } catch (Exception e) {
            return LocalDate.MAX;
        }
    }
}