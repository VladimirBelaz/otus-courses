package factory.settings;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;

public class ChromeSettings implements IDriverSettings {

    @Override
    public AbstractDriverOptions settings() {
        return new ChromeOptions();
    }
}
