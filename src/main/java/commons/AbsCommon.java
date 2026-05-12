package commons;

import com.google.inject.Guice;
import modules.PagesModule;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import utils.Waiters;

public abstract class AbsCommon {

    protected WebDriver driver;
    protected Actions actions;
    protected Waiters waiters;

    public AbsCommon(WebDriver driver) {
        this.driver = driver;
        this.waiters = new Waiters(driver);
        this.actions = new Actions(driver);

        Guice.createInjector(new PagesModule(driver)).injectMembers(this);
        PageFactory.initElements(driver, this);
    }
}