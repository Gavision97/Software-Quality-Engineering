package ac.il.bgu.qa.errors;

/**
 * Represents a custom exception thrown when an operation is
 * attempted on a book that has not been borrowed.
 */
public class BookNotBorrowedException extends RuntimeException {

    /**
     * Constructs a new BookNotBorrowedException with the specified detail message.
     *
     * @param message the detail message.
     */
    public BookNotBorrowedException(String message) {
        super(message);
    }
}
