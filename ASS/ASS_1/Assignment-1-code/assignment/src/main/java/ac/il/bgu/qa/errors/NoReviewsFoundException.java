package ac.il.bgu.qa.errors;

/**
 * Represents a custom exception that is thrown when no reviews are found
 * for a specific book.
 */
public class NoReviewsFoundException extends RuntimeException {

    /**
     * Constructs a new NoReviewsFoundException with the specified detail message.
     *
     * @param message the detail message.
     */
    public NoReviewsFoundException(String message) {
        super(message);
    }
}
