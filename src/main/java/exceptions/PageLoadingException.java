package exceptions;

public class PageLoadingException extends RuntimeException {

    public PageLoadingException(String url, Throwable cause) {
        super("Не удалось загрузить страницу: " + url, cause);
    }

    public PageLoadingException(String message) {
        super(message);
    }
}