package ac.il.bgu.qa;

/**
 * Represents a book with its essential details and borrowing status.
 */
public class Book {
    // The International Standard Book Number (ISBN) uniquely identifying the book.
    private final String ISBN;
    // The title of the book.
    private final String title;
    // The name of the author of the book.
    private final String author;
    // Status to check if the book is currently borrowed or not.
    private boolean isBorrowed;

    /**
     * Constructs a new Book object.
     *
     * @param ISBN   The International Standard Book Number (ISBN) of the book.
     * @param title  The title of the book.
     * @param author The author of the book.
     */
    public Book(String ISBN, String title, String author) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.isBorrowed = false;
    }

    // Getter methods

    /**
     * Retrieves the ISBN of the book.
     *
     * @return The book's ISBN.
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * Retrieves the title of the book.
     *
     * @return The book's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Retrieves the author of the book.
     *
     * @return The book's author.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Checks the borrowing status of the book.
     *
     * @return true if the book is borrowed, otherwise false.
     */
    public boolean isBorrowed() {
        return isBorrowed;
    }

    /**
     * Marks the book as borrowed.
     * Throws an exception if the book is already borrowed.
     */
    public void borrow() {
        if (!isBorrowed) {
            isBorrowed = true;
        } else {
            throw new IllegalStateException("Book is already borrowed!");
        }
    }

    /**
     * Marks the book as returned/not borrowed.
     * Throws an exception if the book was not previously borrowed.
     */
    public void returnBook() {
        if (isBorrowed) {
            isBorrowed = false;
        } else {
            throw new IllegalStateException("ac.il.bgu.qa.Book wasn't borrowed!");
        }
    }
}
