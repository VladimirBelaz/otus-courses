package exceptions;

public class BrowserNotSupportedException extends RuntimeException {

    public BrowserNotSupportedException(String browser) {
        super(String.format("Browser %s not supported", browser));
    }

}
