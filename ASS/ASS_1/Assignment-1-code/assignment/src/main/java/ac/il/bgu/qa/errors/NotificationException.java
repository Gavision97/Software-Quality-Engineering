package ac.il.bgu.qa.errors;

/**
 * Represents a custom exception that is thrown when an issue related
 * to notifications occurs.
 */
public class NotificationException extends RuntimeException {

    /**
     * Constructs a new NotificationException with the specified detail message.
     *
     * @param message the detail message.
     */
    public NotificationException(String message) {
        super(message);
    }
}
