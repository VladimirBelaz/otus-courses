package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateHelper {

    // Форматтер для парсинга русских дат
    private static final DateTimeFormatter RUSSIAN_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("d MMMM yyyy")
            .toFormatter(new Locale("ru"));

    private static final DateTimeFormatter RUSSIAN_FORMATTER_WITH_COMMA = new DateTimeFormatterBuilder()
            .appendPattern("d MMMM, yyyy")
            .toFormatter(new Locale("ru"));

    // Извлечение даты из текста
    public static String extractDateFromText(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }

        // Паттерн для поиска даты: "10 июня 2026" или "10 июня, 2026"
        Pattern pattern = Pattern.compile("(\\d{1,2}\\s+[а-я]+\\s*,?\\s+\\d{4})");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            String dateStr = matcher.group(1);
            return dateStr.replace(",", "");
        }
        return null;
    }

    // Парсинг русской даты
    public static LocalDate parseRussianDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }

        // Проверка на "будет объявлено позже" и подобные
        if (dateStr.contains("будет объявлено") || dateStr.contains("Август") && !dateStr.contains(",")) {
            return null;
        }

        try {
            // Очищаем от лишнего текста (например "27 мая, 2026 · 5 месяцев")
            String cleanDate = dateStr.split("·")[0].trim();

            // Пробуем формат с запятой
            try {
                return LocalDate.parse(cleanDate, RUSSIAN_FORMATTER_WITH_COMMA);
            } catch (Exception e1) {
                // Пробуем формат без запятой
                return LocalDate.parse(cleanDate, RUSSIAN_FORMATTER);
            }
        } catch (Exception e) {
            return null;
        }
    }

    // Проверка, что дата после целевой
    public static boolean isAfterDate(String dateStr, LocalDate targetDate) {
        LocalDate courseDate = parseRussianDate(dateStr);
        return courseDate != null && courseDate.isAfter(targetDate);
    }
}