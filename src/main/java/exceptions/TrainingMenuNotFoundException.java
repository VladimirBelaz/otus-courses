package exceptions;

public class TrainingMenuNotFoundException extends RuntimeException {

    public TrainingMenuNotFoundException() {
        super("Элемент 'Обучение' не найден на странице");
    }

    public TrainingMenuNotFoundException(String message) {
        super(message);
    }
}