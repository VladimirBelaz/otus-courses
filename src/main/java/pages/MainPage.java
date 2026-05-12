package pages;

import annotations.Path;
import components.CookiePopupComponent;
import components.HeaderComponent;
import jakarta.inject.Inject;
import org.openqa.selenium.WebDriver;

@Path("/")
public class MainPage extends AbsBasePage<MainPage> {

    @Inject
    private HeaderComponent header;

    @Inject
    private CookiePopupComponent cookiePopup;

    public MainPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public MainPage open() {
        driver.get(baseUrl + getPath());
        driver.manage().window().maximize();
        cookiePopup.waitAndClose();
        return this;
    }

    public HeaderComponent getHeader() {
        return header;
    }
}