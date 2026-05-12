package pages;

import elements.TextBlock;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;

public class CoursePage extends AbsBasePage<CoursePage> {

    public CoursePage(WebDriver driver) {
        super(driver);
    }

    public String getCourseTitle() {
        WebElement titleElement = waiters.waitForVisibility(By.cssSelector("h1"));
        TextBlock titleBlock = new TextBlock(titleElement);
        return titleBlock.getText();
    }

    public CourseData getCourseDataViaJsoup() {
        String url = driver.getCurrentUrl();
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(10000)
                    .get();

            String title = doc.select("h1").text();
            String startDate = doc.select(".sc-157icee-1").text();

            return new CourseData(title, startDate);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить страницу через Jsoup: " + url, e);
        }
    }

    public record CourseData(String title, String startDate) {}
}