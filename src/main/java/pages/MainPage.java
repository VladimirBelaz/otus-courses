package pages;

import annotations.Path;
import components.HeaderComponent;
import jakarta.inject.Inject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Path("/")
public class MainPage extends AbsBasePage<MainPage> {

    @Inject
    private HeaderComponent header;

    public MainPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public MainPage open() {
        driver.get(baseUrl + getPath());
        driver.manage().window().maximize();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState").equals("complete"));

        return this;
    }

    public HeaderComponent getHeader() {
        return header;
    }
}