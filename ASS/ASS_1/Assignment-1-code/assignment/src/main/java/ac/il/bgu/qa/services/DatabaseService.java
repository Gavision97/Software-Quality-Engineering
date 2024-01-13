package ac.il.bgu.qa.services;

import ac.il.bgu.qa.Book;
import ac.il.bgu.qa.User;

/**
 * Provides an interface for services responsible for managing the database of books and users.
 */
public interface DatabaseService {

    /**
     * Adds a book to the database.
     *
     * @param ISBN The International Standard Book Number identifying the book.
     * @param book The book to be added.
     */
    void addBook(String ISBN, Book book);

    /**
     * Registers a user with the database.
     *
     * @param id   The unique identifier for the user.
     * @param user The user to be registered.
     */
    void registerUser(String id, User user);

    /**
     * Fetches a book from the database using its ISBN.
     *
     * @param ISBN The International Standard Book Number.
     * @return The book with the given ISBN or null if the book does not exist in the database.
     */
    Book getBookByISBN(String ISBN);

    /**
     * Fetches a user from the database using their ID.
     *
     * @param userId The unique identifier for the user.
     * @return The user with the given ID or null if the user is not registered in the database.
     */
    User getUserById(String userId);

    /**
     * Borrows a book identified by its ISBN for a user identified by their userId.
     * This method should appropriately mark the book as borrowed and associate it with the user.
     *
     * @param ISBN   The International Standard Book Number of the book to be borrowed.
     * @param userId The unique identifier for the user borrowing the book.
     */
    void borrowBook(String ISBN, String userId);

    /**
     * Marks a book identified by its ISBN as returned in the database.
     * This should update the book's status to not being borrowed.
     *
     * @param ISBN The International Standard Book Number of the book to be returned.
     */
    void returnBook(String ISBN);
}
