package exceptions;

public class CourseNotFoundException extends RuntimeException {

    public CourseNotFoundException(String courseName) {
        super("Курс с названием '" + courseName + "' не найден в каталоге");
    }

    public CourseNotFoundException(String courseName, Throwable cause) {
        super("Курс с названием '" + courseName + "' не найден в каталоге", cause);
    }
}