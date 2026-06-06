package stepdefinitions;

import helpers.CourseCardExtractor;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import listeners.DriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.CatalogPage;
import utils.DateHelper;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

import static helpers.CourseCardExtractor.getCourseTitleFromCard;
import static utils.PriceHelper.extractNumberFromString;

public class FilterSteps {

    private WebDriver driver;
    private List<CoursePriceInfo> coursesWithPrices;
    private List<CourseDateInfo> coursesWithDates;

    private record CourseDateInfo(String title, String dateStr, LocalDate date) {}
    private record CoursePriceInfo(String title, int price) {}

    public FilterSteps() {
        this.driver = DriverManager.getDriver();
    }

    @Когда("я ищу курсы, стартующие позже {string}")
    public void searchCoursesAfterDate(String targetDateStr) {
        LocalDate targetDate = LocalDate.parse(targetDateStr);
        coursesWithDates = new ArrayList<>();

        CatalogPage catalogPage = new CatalogPage(driver);
        catalogPage.open();

        loadAllCourses();

        List<WebElement> courseCards = driver.findElements(By.cssSelector("a[href*='/lessons/']"));

        for (WebElement card : courseCards) {
            String title = getCourseTitleFromCard(card);
            String dateStr = CourseCardExtractor.getCourseDate(card);

            if (title == null || dateStr == null || dateStr.contains("будет объявлено позже")) {
                continue;
            }

            LocalDate courseDate = DateHelper.parseRussianDate(dateStr);
            if (courseDate != null && courseDate.isAfter(targetDate)) {
                coursesWithDates.add(new CourseDateInfo(title, dateStr, courseDate));
            }
        }

        System.out.println("Найдено курсов после " + targetDateStr + ": " + coursesWithDates.size());
    }

    private void loadAllCourses() {
        for (int i = 0; i < 5; i++) {
            try {
                WebElement showMore = driver.findElement(By.cssSelector(".sc-1qig7zt-0.bYRRHi.sc-prqxfo-0.cXVWAS"));
                if (showMore.isDisplayed()) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", showMore);
                    showMore.click();
                } else {
                    break;
                }
            } catch (Exception e) {
                break;
            }
        }
    }

    @Тогда("я вывожу в консоль список найденных курсов")
    public void printFoundCourses() {
        if (coursesWithDates == null || coursesWithDates.isEmpty()) {
            System.out.println("Нет курсов, стартующих позже указанной даты");
            return;
        }

        System.out.println("\n=== СПИСОК КУРСОВ ===");
        int counter = 1;
        for (CourseDateInfo course : coursesWithDates) {
            System.out.println(counter++ + ". " + course.title() + " — " + course.dateStr());
        }
        System.out.println("Всего: " + coursesWithDates.size() + " курсов\n");
    }

    @Тогда("я нахожу и вывожу самый дорогой и самый дешёвый курс")
    public void findAndPrintMostExpensiveAndCheapestCourse() {
        extractCoursesWithPrices();

        if (coursesWithPrices.isEmpty()) {
            System.out.println("Не найдено курсов с ценами");
            return;
        }

        CoursePriceInfo mostExpensive = coursesWithPrices.stream()
                .max(Comparator.comparingInt(CoursePriceInfo::price))
                .orElse(null);

        CoursePriceInfo cheapest = coursesWithPrices.stream()
                .min(Comparator.comparingInt(CoursePriceInfo::price))
                .orElse(null);

        System.out.println("\n=== САМЫЙ ДОРОГОЙ КУРС ===");
        System.out.println("Название: " + mostExpensive.title());
        System.out.println("Цена: " + mostExpensive.price() + " ₽");

        System.out.println("\n=== САМЫЙ ДЕШЁВЫЙ КУРС ===");
        System.out.println("Название: " + cheapest.title());
        System.out.println("Цена: " + cheapest.price() + " ₽");
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

                showMoreButton.click();
                System.out.println("Нажата кнопка 'Показать еще 3'");

                // Проверяем, что кнопка исчезла или стала неактивной
                try {
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


}