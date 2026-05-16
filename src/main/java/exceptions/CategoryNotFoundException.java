package exceptions;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException() {
        super("Не найдено категорий в меню 'Обучение'");
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }
}