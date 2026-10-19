package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;
import java.util.Map;

public class DateUtils {

    // OTUS показывает месяцы в РОДИТЕЛЬНОМ падеже — стандартный Locale("ru") их не парсит
    private static final Map<Long, String> RU_MONTHS_GENITIVE = Map.ofEntries(
            Map.entry(1L,  "января"),
            Map.entry(2L,  "февраля"),
            Map.entry(3L,  "марта"),
            Map.entry(4L,  "апреля"),
            Map.entry(5L,  "мая"),
            Map.entry(6L,  "июня"),
            Map.entry(7L,  "июля"),
            Map.entry(8L,  "августа"),
            Map.entry(9L,  "сентября"),
            Map.entry(10L, "октября"),
            Map.entry(11L, "ноября"),
            Map.entry(12L, "декабря")
    );

    // "d " (один d) — потому что на странице бывает "5 мая" с однозначным днём
    private static final DateTimeFormatter RUSSIAN_FORMATTER =
            new DateTimeFormatterBuilder()
                    .appendPattern("d ")
                    .appendText(ChronoField.MONTH_OF_YEAR, RU_MONTHS_GENITIVE)
                    .appendPattern(", yyyy")
                    .toFormatter(Locale.ROOT);

    public static LocalDate parseRussianDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return LocalDate.MAX;
        }
        try {
            // "30 июня, 2026 · 3 месяца" -> "30 июня, 2026"
            String cleanDate = dateStr.split("·")[0]
                    .replaceAll("\\s+", " ")
                    .trim();
            return LocalDate.parse(cleanDate, RUSSIAN_FORMATTER);
        } catch (Exception e) {
            return LocalDate.MAX;
        }
    }
}