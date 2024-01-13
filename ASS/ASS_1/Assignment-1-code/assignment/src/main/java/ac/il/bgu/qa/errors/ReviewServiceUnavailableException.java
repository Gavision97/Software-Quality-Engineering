package ac.il.bgu.qa.errors;

/**
 * Represents a custom exception that is thrown when the ReviewService 
 * is not available or encounters an issue.
 */
public class ReviewServiceUnavailableException extends RuntimeException {

    /**
     * Constructs a new ReviewServiceUnavailableException with the specified detail message.
     *
     * @param message the detail message. 
     */
    public ReviewServiceUnavailableException(String message) {
        super(message);
    }
}
