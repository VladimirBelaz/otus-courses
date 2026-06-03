package stepdefinitions;

import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import listeners.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.CatalogPage;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterSteps {

    private WebDriver driver;
    private List<CoursePriceInfo> coursesWithPrices;
    private List<CourseDateInfo> coursesWithDates;
    private boolean coursesLoaded = false;

    private record CourseDateInfo(String title, String dateStr, LocalDate date, String url) {}

    public FilterSteps() {
        this.driver = DriverManager.getDriver();
    }

    private void loadCoursesOnce() {
        if (!coursesLoaded) {
            extractCoursesWithPrices();
            coursesLoaded = true;
        }
    }

    @Когда("я ищу курсы, стартующие позже {string}")
    public void searchCoursesAfterDate(String targetDateStr) {
        if (driver == null) {
            driver = DriverManager.getDriver();
        }

        LocalDate targetDate = LocalDate.parse(targetDateStr);
        coursesWithDates = new ArrayList<>();

        CatalogPage catalogPage = new CatalogPage(driver);
        catalogPage.open();

        // Нажимаем кнопку "Показать еще" несколько раз
        for (int i = 0; i < 5; i++) {
            try {
                WebElement showMore = driver.findElement(By.cssSelector(".sc-1qig7zt-0.bYRRHi.sc-prqxfo-0.cXVWAS"));
                if (showMore.isDisplayed()) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", showMore);
                    Thread.sleep(500);
                    showMore.click();
                    Thread.sleep(1000);
                } else {
                    break;
                }
            } catch (Exception e) {
                break;
            }
        }

        List<WebElement> courseCards = driver.findElements(By.cssSelector("a[href*='/lessons/']"));

        for (WebElement card : courseCards) {
            try {
                String title = getCourseTitleFromCard(card);
                if (title == null || title.isEmpty()) continue;

                String dateStr = getDateFromCard(card);
                if (dateStr == null || dateStr.isEmpty()) continue;

                if (dateStr.contains("будет объявлено позже")) continue;

                LocalDate courseDate = parseDate(dateStr);
                if (courseDate == null) continue;

                if (courseDate.isAfter(targetDate)) {
                    coursesWithDates.add(new CourseDateInfo(title, dateStr, courseDate, null));
                }
            } catch (Exception e) {
                // игнорируем
            }
        }

        System.out.println("\nНайдено курсов, стартующих после " + targetDateStr + ": " + coursesWithDates.size());

        if (!coursesWithDates.isEmpty()) {
            System.out.println("\nСписок курсов:");
            for (CourseDateInfo course : coursesWithDates) {
                System.out.println("  - " + course.title() + " — " + course.dateStr());
            }
        }
    }

    @Тогда("я вывожу в консоль список найденных курсов")
    public void printFoundCourses() {
        if (coursesWithDates == null || coursesWithDates.isEmpty()) {
            System.out.println("Нет курсов, стартующих позже указанной даты");
            return;
        }

        System.out.println("\n=== СПИСОК КУРСОВ, СТАРТУЮЩИХ ПОСЛЕ УКАЗАННОЙ ДАТЫ ===");
        System.out.println("┌────┬──────────────────────────────────────────┬─────────────────┐");
        System.out.println("│ №  │ Название курса                          │ Дата старта     │");
        System.out.println("├────┼──────────────────────────────────────────┼─────────────────┤");

        int counter = 1;
        for (CourseDateInfo course : coursesWithDates) {
            String truncatedTitle = course.title().length() > 40
                    ? course.title().substring(0, 37) + "..."
                    : course.title();
            System.out.printf("│ %-2d │ %-40s │ %-15s │\n",
                    counter++, truncatedTitle, course.dateStr());
        }
        System.out.println("└────┴──────────────────────────────────────────┴─────────────────┘");
        System.out.println("Всего найдено: " + coursesWithDates.size() + " курсов\n");
    }

    // Вспомогательный метод для получения названия курса из карточки
    private String getCourseTitleFromCard(WebElement card) {
        String[] selectors = {
                ".sc-1yg5ro0-0",
                ".frUeNO",
                "h6",
                ".sc-1yg5ro0-1",
                "[class*='sc-1yg5ro0']",
                "h6 div"
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
                // продолжаем поиск
            }
        }
        return null;
    }

    // Вспомогательный метод для получения даты из карточки
    private String getDateFromCard(WebElement card) {
        try {
            // Получаем весь текст карточки
            String cardText = card.getText();

            // Ищем паттерн даты: "10 июня, 2026" или "10 июня 2026"
            Pattern pattern = Pattern.compile("(\\d{1,2}\\s+[а-я]+\\s*,?\\s+\\d{4})");
            Matcher matcher = pattern.matcher(cardText);

            if (matcher.find()) {
                String dateStr = matcher.group(1);
                // Убираем лишнюю запятую если есть
                dateStr = dateStr.replace(",", "");
                return dateStr;
            }
        } catch (Exception e) {
            System.out.println("Ошибка при поиске даты в тексте: " + e.getMessage());
        }

        return null;
    }

    // Парсинг русской даты
    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }

        try {
            // Формат: "10 июня 2026"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", new Locale("ru"));
            return LocalDate.parse(dateStr, formatter);
        } catch (Exception e) {
            try {
                // Формат: "10 июня, 2026" (с запятой)
                String cleaned = dateStr.replace(",", "");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", new Locale("ru"));
                return LocalDate.parse(cleaned, formatter);
            } catch (Exception ex) {
                System.out.println("Не удалось распарсить дату: " + dateStr);
                return null;
            }
        }
    }

    @Тогда("я нахожу и вывожу самый дорогой и самый дешёвый курс")
    public void findAndPrintMostExpensiveAndCheapestCourseWithStreamFilter() {
        // Используем существующий метод для сбора курсов с ценами
        extractCoursesWithPrices();

        if (coursesWithPrices == null || coursesWithPrices.isEmpty()) {
            System.out.println("Не найдено курсов с ценами");
            return;
        }

        // ========== ИСПОЛЬЗОВАНИЕ STREAM FILTER ==========
        List<CoursePriceInfo> validCourses = coursesWithPrices.stream()
                .filter(c -> c.price() > 0)
                .filter(c -> c.title() != null && !c.title().isEmpty())
                .toList();

        if (validCourses.isEmpty()) {
            System.out.println("Нет валидных курсов");
            return;
        }

        CoursePriceInfo mostExpensive = validCourses.stream()
                .max(Comparator.comparingInt(CoursePriceInfo::price))
                .orElse(null);

        CoursePriceInfo cheapest = validCourses.stream()
                .min(Comparator.comparingInt(CoursePriceInfo::price))
                .orElse(null);

        System.out.println("\n=== САМЫЙ ДОРОГОЙ КУРС ===");
        System.out.println("Название: " + mostExpensive.title());
        System.out.println("Цена: " + mostExpensive.price() + " ₽");

        System.out.println("\n=== САМЫЙ ДЕШЁВЫЙ КУРС ===");
        System.out.println("Название: " + cheapest.title());
        System.out.println("Цена: " + cheapest.price() + " ₽");
    }

    // Вспомогательный метод для получения цены из карточки (без перехода на страницу)
    private int getPriceFromCard(WebElement card) {
        // Селекторы для поиска цены в карточке курса
        String[] selectors = {
                ".sc-153sikp-11",           // основной селектор цены
                ".sc-153sikp-11.gztHyx",    // альтернативный вариант
                ".tn-atom",                  // для страниц на Tilda
                "[class*='price']",          // любой элемент с price в классе
                "[class*='Price']"           // с заглавной P
        };

        for (String selector : selectors) {
            try {
                List<WebElement> elements = card.findElements(By.cssSelector(selector));
                for (WebElement el : elements) {
                    String text = el.getText();
                    if (text != null && !text.isEmpty()) {
                        int price = extractNumberFromString(text);
                        if (price > 0 && price < 500000) {
                            System.out.println("  Найдена цена: " + price + " ₽ по селектору: " + selector);
                            return price;
                        }
                    }
                }
            } catch (Exception e) {
                // игнорируем
            }
        }

        return 0;
    }

    @Тогда("я нахожу самый дорогой курс")
    public void findMostExpensiveCourse() {
        loadCoursesOnce();  // ← загружаем только один раз

        if (coursesWithPrices.isEmpty()) {
            System.out.println("Не найдено курсов с ценами");
            return;
        }

        CoursePriceInfo mostExpensive = coursesWithPrices.stream()
                .max((c1, c2) -> Integer.compare(c1.price, c2.price))
                .orElse(null);

        if (mostExpensive != null) {
            System.out.println("=== САМЫЙ ДОРОГОЙ КУРС ===");
            System.out.println("Название: " + mostExpensive.title);
            System.out.println("Цена: " + mostExpensive.price + " ₽");
        }
    }

    @Тогда("я вывожу информацию о самом дорогом курсе в консоль")
    public void printMostExpensiveCourse() {
        loadCoursesOnce();
        // тот же код, что и в findMostExpensiveCourse()
        if (coursesWithPrices.isEmpty()) {
            System.out.println("Не найдено курсов с ценами");
            return;
        }

        CoursePriceInfo mostExpensive = coursesWithPrices.stream()
                .max((c1, c2) -> Integer.compare(c1.price, c2.price))
                .orElse(null);

        if (mostExpensive != null) {
            System.out.println("=== САМЫЙ ДОРОГОЙ КУРС ===");
            System.out.println("Название: " + mostExpensive.title);
            System.out.println("Цена: " + mostExpensive.price + " ₽");
        }
    }

    @Тогда("я нахожу самый дешёвый курс")
    public void findCheapestCourse() {
        loadCoursesOnce();

        if (coursesWithPrices.isEmpty()) {
            System.out.println("Не найдено курсов с ценами");
            return;
        }

        CoursePriceInfo cheapest = coursesWithPrices.stream()
                .min((c1, c2) -> Integer.compare(c1.price, c2.price))
                .orElse(null);

        if (cheapest != null) {
            System.out.println("=== САМЫЙ ДЕШЁВЫЙ КУРС ===");
            System.out.println("Название: " + cheapest.title);
            System.out.println("Цена: " + cheapest.price + " ₽");
        }
    }

    @Тогда("я вывожу информацию о самом дешёвом курсе в консоль")
    public void printCheapestCourse() {
        loadCoursesOnce();

        if (coursesWithPrices.isEmpty()) {
            System.out.println("Не найдено курсов с ценами");
            return;
        }

        CoursePriceInfo cheapest = coursesWithPrices.stream()
                .min((c1, c2) -> Integer.compare(c1.price, c2.price))
                .orElse(null);

        if (cheapest != null) {
            System.out.println("=== САМЫЙ ДЕШЁВЫЙ КУРС ===");
            System.out.println("Название: " + cheapest.title);
            System.out.println("Цена: " + cheapest.price + " ₽");
        }
    }

    private void extractCoursesWithPrices() {
        if (driver == null) {
            driver = DriverManager.getDriver();
        }

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Ждём загрузки карточек курсов
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[href^='/online/']")));
        } catch (TimeoutException e) {
            System.out.println("Не удалось загрузить карточки курсов");
            return;
        }

        // Нажимаем кнопку "Показать еще 3", если она есть
        try {
            WebElement showMoreButton = driver.findElement(By.cssSelector(".sc-1qig7zt-0.bYRRHi.sc-prqxfo-0.cXVWAS"));
            if (showMoreButton.isDisplayed() && showMoreButton.isEnabled()) {
                // Прокручиваем к кнопке
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", showMoreButton);
                Thread.sleep(500);

                showMoreButton.click();
                System.out.println("Нажата кнопка 'Показать еще 3'");

                // Ждём появления новых курсов
                Thread.sleep(2000);

                // Проверяем, что кнопка исчезла или стала неактивной
                try {
                    Thread.sleep(1000);
                    if (showMoreButton.isDisplayed()) {
                        System.out.println("Кнопка всё ещё видна, возможно, есть ещё курсы");
                    } else {
                        System.out.println("Кнопка исчезла, все курсы загружены");
                    }
                } catch (Exception ex) {
                    System.out.println("Кнопка больше не доступна, все курсы загружены");
                }
            }
        } catch (Exception e) {
            System.out.println("Кнопка 'Показать еще' не найдена или неактивна");
        }

        // Собираем уникальные курсы после нажатия кнопки
        Map<String, String> uniqueCourses = new LinkedHashMap<>();
        List<WebElement> courseLinks = driver.findElements(By.cssSelector("a[href^='/online/']"));

        System.out.println("Найдено элементов (после нажатия кнопки): " + courseLinks.size());

        for (WebElement link : courseLinks) {
            try {
                String href = link.getAttribute("href");
                if (href == null || href.isEmpty()) continue;

                // Очищаем URL от якорей и параметров
                href = href.split("#")[0].split("\\?")[0];

                if (uniqueCourses.containsKey(href)) {
                    continue;
                }

                // Получаем название курса
                String title = getCourseTitleFromCard(link);
                if (title == null || title.isEmpty()) {
                    String slug = href.substring(href.lastIndexOf("/") + 1);
                    title = "Курс без названия (" + slug + ")";
                }

                uniqueCourses.put(href, title);

            } catch (Exception e) {
                System.out.println("Ошибка при чтении карточки: " + e.getMessage());
            }
        }

        System.out.println("Уникальных подготовительных курсов: " + uniqueCourses.size());

        coursesWithPrices = new ArrayList<>();

        int counter = 1;
        for (Map.Entry<String, String> entry : uniqueCourses.entrySet()) {
            String url = entry.getKey();
            String title = entry.getValue();

            System.out.print(counter++ + "/" + uniqueCourses.size() + " Обрабатываю курс: " + title + "... ");

            try {
                driver.get(url);
                Thread.sleep(1000);

                int price = extractPriceFromCoursePage(wait);

                if (price > 0) {
                    coursesWithPrices.add(new CoursePriceInfo(title, price));
                    System.out.println("✅ цена: " + price + " ₽");
                } else {
                    System.out.println("❌ цена не найдена");
                }
            } catch (Exception e) {
                System.out.println("❌ ошибка: " + e.getMessage());
            }
        }

        System.out.println("Всего курсов с ценами: " + coursesWithPrices.size());
    }

    private int extractPriceFromCoursePage(WebDriverWait wait) {
        try {
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
        } catch (Exception e) {
            // игнорируем
        }

        // Специальные селекторы для страниц подготовительных курсов на Tilda
        String[] priceSelectors = {
                ".tn-atom[field*='tn_text']",           // атрибут field содержит текст
                "[field*='tn_text_1780482750682000003']", // конкретный field для цены
                ".tn-atom",                              // общий селектор
                ".sc-153sikp-11",                        // старый формат
                "[class*='price']"                       // запасной вариант
        };

        for (String selector : priceSelectors) {
            try {
                List<WebElement> priceElements = driver.findElements(By.cssSelector(selector));

                for (int i = 0; i < priceElements.size(); i++) {
                    try {
                        // Перезахватываем элемент на каждой итерации
                        WebElement element = driver.findElements(By.cssSelector(selector)).get(i);
                        String text = element.getText();

                        if (text != null && !text.isEmpty()) {
                            // Проверяем, что это похоже на цену (есть число и ₽)
                            if (text.matches(".*\\d+.*₽.*") || text.matches(".*₽.*\\d+.*")) {
                                int price = extractNumberFromString(text);
                                if (price > 0 && price < 500000) {
                                    System.out.println("  Найдена цена: " + price + " ₽");
                                    return price;
                                }
                            }
                        }
                    } catch (StaleElementReferenceException e) {
                        // Пропускаем этот элемент и пробуем следующий
                        continue;
                    } catch (Exception e) {
                        continue;
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }

        return 0;
    }

    private int extractNumberFromString(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }

        // Ищем числа, которые идут перед символом ₽ или после
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

        // Альтернативный паттерн: просто число в тексте (если нет ₽)
        Pattern numberPattern = Pattern.compile("(\\d+[\\s\\d]*)");
        Matcher numberMatcher = numberPattern.matcher(text);
        if (numberMatcher.find()) {
            String priceStr = numberMatcher.group(1).replaceAll("\\s", "");
            try {
                int price = Integer.parseInt(priceStr);
                // Если число в разумных пределах (не номер курса)
                if (price > 1000 && price < 500000) {
                    return price;
                }
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        return 0;
    }

    private record CoursePriceInfo(String title, int price) {}
}