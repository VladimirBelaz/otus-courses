package factory.settings;

import org.openqa.selenium.remote.AbstractDriverOptions;

public interface IDriverSettings<T extends AbstractDriverOptions<T>> {

    AbstractDriverOptions<T> settings();

}
