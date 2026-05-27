package pages;

import annotations.Path;
import commons.AbsCommon;
import org.openqa.selenium.WebDriver;


public abstract class AbsBasePage<T> extends AbsCommon {

    protected String baseUrl = System.getProperty("base.url");

    public AbsBasePage(WebDriver driver) {
        super(driver);
    }

    protected String getPath() {
        Class<?> clazz = this.getClass();
        if (clazz.isAnnotationPresent(Path.class)) {
            Path path = clazz.getDeclaredAnnotation(Path.class);
            return path.value();
        }
        return "";
    }

    @SuppressWarnings("unchecked")
    public T open() {
        String url = baseUrl + getPath();
        driver.get(url);
        return (T) this;
    }

    public String getCurrentUrl() {

        return driver.getCurrentUrl();
    }
}