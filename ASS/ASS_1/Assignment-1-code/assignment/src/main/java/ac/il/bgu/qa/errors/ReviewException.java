package ac.il.bgu.qa.errors;

/**
 * Represents a custom exception that is thrown when an issue related
 * to book reviews occurs.
 */
public class ReviewException extends RuntimeException {

    /**
     * Constructs a new ReviewException with the specified detail message.
     *
     * @param message the detail message.
     */
    public ReviewException(String message) {
        super(message);
    }
}
