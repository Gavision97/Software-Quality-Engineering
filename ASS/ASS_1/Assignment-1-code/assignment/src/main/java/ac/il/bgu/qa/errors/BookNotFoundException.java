package ac.il.bgu.qa.errors;

/**
 * Represents a custom exception that is thrown when a specific book
 * is not found in the library's database.
 */
public class BookNotFoundException extends RuntimeException {

    /**
     * Constructs a new BookNotFoundException with the specified detail message.
     *
     * @param message the detail message.
     */
    public BookNotFoundException(String message) {
        super(message);
    }
}
