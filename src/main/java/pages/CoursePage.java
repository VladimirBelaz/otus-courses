package pages;

import elements.TextBlock;
import exceptions.ElementInteractionException;
import exceptions.PageLoadingException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import java.io.IOException;

public class CoursePage extends AbsBasePage<CoursePage> {

    private static final By TITLE = By.cssSelector("h1");

    private static final By START_DATE =
            By.cssSelector(".sc-157icee-1");

    public CoursePage(WebDriver driver) {
        super(driver);
    }

    public String getCourseTitle() {
        try {
            WebElement titleElement = waiters.waitForVisibility(TITLE);
            TextBlock titleBlock = new TextBlock(titleElement);
            return titleBlock.getText();
        } catch (WebDriverException e) {
            throw new ElementInteractionException("Получение заголовка курса", e);
        }
    }

    public String getStartDate() {
        try {
            WebElement dateElement = waiters.waitForVisibility(START_DATE);
            TextBlock dateBlock = new TextBlock(dateElement);
            return dateBlock.getText();
        } catch (Exception e) {
            return null;
        }
    }

    public CourseData getCourseDataViaJsoup() {
        String url = driver.getCurrentUrl();
        try {
            Document document = Jsoup.connect(url).get();

            return new CourseData(
                    document.select("h1").text(),
                    document.select(".sc-157icee-1").text()
            );
        } catch (IOException e) {
            throw new PageLoadingException(url, e);
        }
    }

    public record CourseData(String title, String startDate) {
    }
}