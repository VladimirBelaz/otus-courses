package exceptions;

public class ElementInteractionException extends RuntimeException {

    public ElementInteractionException(String elementName, Throwable cause) {
        super("Не удалось взаимодействовать с элементом: " + elementName, cause);
    }

    public ElementInteractionException(String message) {
        super(message);
    }
}