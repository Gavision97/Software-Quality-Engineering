package ac.il.bgu.qa.errors;

/**
 * Represents a custom exception that is thrown when a specific user is not found
 * or registered within the library's system.
 */
public class UserNotRegisteredException extends RuntimeException {

    /**
     * Constructs a new UserNotRegisteredException with the specified detail message.
     *
     * @param message the detail message.
     */
    public UserNotRegisteredException(String message) {
        super(message);
    }
}
