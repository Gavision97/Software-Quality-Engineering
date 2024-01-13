package ac.il.bgu.qa.errors;

/**
 * Represents a custom exception thrown when an attempt is made
 * to borrow a book that has already been borrowed.
 */
public class BookAlreadyBorrowedException extends RuntimeException {

    /**
     * Constructs a new BookAlreadyBorrowedException with the specified detail message.
     *
     * @param message the detail message.
     */
    public BookAlreadyBorrowedException(String message) {
        super(message);
    }
}
