package ac.il.bgu.qa;

import ac.il.bgu.qa.errors.*;
import ac.il.bgu.qa.services.DatabaseService;
import ac.il.bgu.qa.services.ReviewService;

import java.util.*;

/**
 * Represents a library which manages a collection of books and users.
 */
public class Library {

    // Service to interact with the database
    private final DatabaseService databaseService;

    // Service to fetch reviews for a book
    private final ReviewService reviewService;

    // Constructor for Library, initializes both services
    public Library(DatabaseService databaseService, ReviewService reviewService) {
        this.databaseService = databaseService;
        this.reviewService = reviewService;
    }

    /**
     * Adds a book to the library's collection.
     *
     * @param book The book to be added.
     */
    public void addBook(Book book) {
        // Multiple checks to validate the book object's properties
        if (book == null) {
            throw new IllegalArgumentException("Invalid book.");
        } else if (!isISBNValid(book.getISBN())){
            throw new IllegalArgumentException("Invalid ISBN.");
        } else if (book.getTitle() == null || book.getTitle().equals("")) {
            throw new IllegalArgumentException("Invalid title.");
        } else if (!isAuthorValid(book.getAuthor())) {
            throw new IllegalArgumentException("Invalid author.");
        } else if (book.isBorrowed()) {
            throw new IllegalArgumentException("Book with invalid borrowed state.");
        }

        // If book already exists in the database, throw exception
        if (databaseService.getBookByISBN(book.getISBN()) != null)
            throw new IllegalArgumentException("Book already exists.");

        // If all checks pass, add the book to the database
        databaseService.addBook(book.getISBN(), book);
    }

    /**
     *  Validates if input is of type ISBN-13.
     *
     *  @param isbn The International Standard Book Number to be validated.
     *  @return true if valid, false otherwise.
    */
    private boolean isISBNValid(String isbn) {
        // Check if the ISBN is null, return false if it is
        if (isbn == null) {
            return false;
        }

        // Remove any hyphen characters from the ISBN for standardization
        isbn = isbn.replaceAll("-", "");

        // Check if the sanitized ISBN is of length 13 and if contains only digits
        if (isbn.length() != 13 || !isbn.matches("\\d+")) {
            return false;
        }

        // Calculate the sum based on the ISBN-13 rules:
        // Odd position numbers are multiplied by 1
        // Even position numbers are multiplied by 3
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Integer.parseInt(String.valueOf(isbn.charAt(i)));
            if (i % 2 == 0) {
                sum += digit;
            } else {
                sum += digit * 3;
            }
        }

        // Calculate the check digit
        int checkDigit = 10 - (sum % 10);
        if (checkDigit == 10) {
            checkDigit = 0;
        }

        // Check if the calculated check digit matches the last digit of the ISBN
        return checkDigit == Integer.parseInt(String.valueOf(isbn.charAt(12)));
    }

    /**
     *  Validates if author name given as input is of valid format.
     *
     *  @param name The name of the author.
     *  @return true if valid, false otherwise.
    */
    private boolean isAuthorValid(String name) {
        /*
         * Three key criteria for a valid author name:
         * 1) The name should only consist of alphabetic characters, hyphens, spaces, dots, and apostrophes.
         * 2) The name should start and end with an alphabetic character.
         * 3) The name should not contain consecutive special characters like "--" or "''".
         */

        // If the name is null or empty, it's invalid.
        if (name == null || name.isEmpty()) {
            return false;
        }

        // Ensure the name starts and finishes with an alphabetic character.
        if (!Character.isLetter(name.charAt(0)) || !Character.isLetter(name.charAt(name.length() - 1))) {
            return false;
        }

        // Check the content of the name for valid characters and consecutive special characters.
        for (int i = 0; i < name.length() - 1; i++) {
            char current = name.charAt(i);
            char next = name.charAt(i + 1);

            // Check if the current character is not one of the valid characters.
            if (!Character.isLetter(current) && current != '-' && current != ' ' && current != '\'' && current != '.') {
                return false;
            }

            // Check for consecutive special characters.
            if ((current == '-' && next == '-')
                    || (current == '\'' && next == '\'')) {
                return false;
            }
        }

        // If all checks have passed, the author name is valid.
        return true;
    }


    /**
     * Registers a user with the library.
     *
     * @param user The user to be registered.
     */
    public void registerUser(User user) {
        // Multiple checks to validate the user object's properties.
        if (user == null) {
            throw new IllegalArgumentException("Invalid user.");
        } else if (user.getId() == null || !user.getId().matches("\\d{12}")) {
            throw new IllegalArgumentException("Invalid user Id.");
        } else if (user.getName() == null || user.getName().equals("")) {
            throw new IllegalArgumentException("Invalid user name.");
        } else if (user.getNotificationService() == null) {
            throw new IllegalArgumentException("Invalid notification service.");
        }

        // Before registering, check if a user with the given Id already exists.
        // If such a user is found, throw an exception.
        if (databaseService.getUserById(user.getId()) != null)
            throw new IllegalArgumentException("User already exists.");

        // If all checks have passed, call the database service to register the user.
        databaseService.registerUser(user.getId(), user);
    }


    /**
     * Borrows a book for a user.
     *
     * @param ISBN   The International Standard Book Number of the book.
     * @param userId The Id of the user borrowing the book.
     */
    public void borrowBook(String ISBN, String userId) {

        // Validate the ISBN. If it's invalid, throw an exception.
        if (!isISBNValid(ISBN)) {
            throw new IllegalArgumentException("Invalid ISBN.");
        }

        // Retrieve the book associated with the ISBN from the database.
        Book book = databaseService.getBookByISBN(ISBN);

        // If no book is found for the given ISBN, throw an exception.
        if (book == null) {
            throw new BookNotFoundException("Book not found!");
        }

        // Validate the user Id's format (should be a 12-digit number).
        // If it's invalid, throw an exception.
        if (userId == null || !userId.matches("\\d{12}")) {
            throw new IllegalArgumentException("Invalid user Id.");
        }

        // Check if the user Id's corresponds to a registered user in the database.
        // If not, throw an exception indicating the user is not registered.
        if (databaseService.getUserById(userId) == null) {
            throw new UserNotRegisteredException("User not found!");
        }

        // If the book is already borrowed, throw an exception.
        if (book.isBorrowed()) {
            throw new BookAlreadyBorrowedException("Book is already borrowed!");
        }

        // Mark the book as borrowed.
        book.borrow();

        // Record the borrowing transaction in the database by associating the book's ISBN with the user's Id.
        databaseService.borrowBook(ISBN, userId);
    }

    /**
     * Returns a previously borrowed book.
     *
     * @param ISBN The International Standard Book Number of the book.
     */
    public void returnBook(String ISBN) {
        
        // Validate the ISBN. If it's not valid, throw an exception.
        if (!isISBNValid(ISBN)) {
            throw new IllegalArgumentException("Invalid ISBN.");
        }

        // Retrieve the book associated with the ISBN from the database.
        Book book = databaseService.getBookByISBN(ISBN);

        // If no book is found for the given ISBN, throw a book not found exception.
        if (book == null) {
            throw new BookNotFoundException("Book not found!");
        }

        // Check if the book is currently borrowed. If not, it means it was never borrowed
        // or it has already been returned, therefore throw an exception.
        if (!book.isBorrowed()) {
            throw new BookNotBorrowedException("Book wasn't borrowed!");
        }

        // Change the status of the book to not borrowed.
        book.returnBook();

        // Update the database to reflect the returned status of the book.
        databaseService.returnBook(ISBN);
    }

    /**
     * Notifies a user with the reviews of a specified book.
     *
     * @param ISBN The ISBN of the book whose reviews are to be sent.
     * @param userId The Id of the user to whom the reviews are to be sent.
     */
    public void notifyUserWithBookReviews(String ISBN, String userId) {

        // Validate the ISBN. If it's invalid, throw an exception.
        if (!isISBNValid(ISBN)) {
            throw new IllegalArgumentException("Invalid ISBN.");
        }

        // Validate the user Id format (should be a 12-digit number).
        // If it's invalid, throw an exception.
        if (userId == null || !userId.matches("\\d{12}")) {
            throw new IllegalArgumentException("Invalid user Id.");
        }

        // Retrieve the book associated with the ISBN from the database.
        Book book = databaseService.getBookByISBN(ISBN);

        // If no book is found for the given ISBN, throw a book not found exception.
        if (book == null) {
            throw new BookNotFoundException("Book not found!");
        }

        // Retrieve the user associated with the user Id from the database.
        User user = databaseService.getUserById(userId);

        // If the user is not found in the database, throw an exception.
        if (user == null) {
            throw new UserNotRegisteredException("User not found!");
        }

        // Fetch the list of reviews for the specified book using the review service.
        List<String> reviews;
        try {
            reviews = reviewService.getReviewsForBook(ISBN);

            // If no reviews are found or the review list is empty, throw an exception.
            if (reviews == null || reviews.isEmpty()) {
                throw new NoReviewsFoundException("No reviews found!");
            }
        } catch (ReviewException e) {
            // If there's an issue fetching the reviews, throw a service unavailable exception.
            throw new ReviewServiceUnavailableException("Review service unavailable!");
        } finally {
            // Always close the review service connection after attempting to fetch the reviews.
            reviewService.close();
        }

        // Construct the notification message containing the book's title and its reviews.
        String notificationMessage = "Reviews for '" + book.getTitle() + "':\n" + String.join("\n", reviews);

        // Attempt to send the notification to the user. If it fails, retry up to 5 times.
        int retryCount = 0;
        while (retryCount < 5) {
            try {
                user.sendNotification(notificationMessage);
                return;
            } catch (NotificationException e) {
                retryCount++;
                System.err.println("Notification failed! Retrying attempt " + retryCount + "/5");
            }
        }

        // If all retry attempts fail, throw a notification exception.
        throw new NotificationException("Notification failed!");
    }

    /**
     * Fetches a book by its ISBN and notifies the user with its reviews.
     *
     * @param ISBN   The International Standard Book Number of the book to be fetched.
     * @param userId The Id of the user to be notified with the book's reviews.
     * @return       The book with the given ISBN if found, and notifies the user with its reviews.
     */
    public Book getBookByISBN(String ISBN, String userId) {
        // Validate the ISBN. If it's invalid, throw an exception.
        if (!isISBNValid(ISBN)) {
            throw new IllegalArgumentException("Invalid ISBN.");
        }

        // Validate the user Id format (should be a 12-digit number). 
        // If it's invalid, throw an exception.
        if (userId == null || !userId.matches("\\d{12}")) {
            throw new IllegalArgumentException("Invalid user Id.");
        }

        // Retrieve the book associated with the ISBN from the database.
        Book book = databaseService.getBookByISBN(ISBN);

        // If no book is found for the given ISBN, throw a book not found exception.
        if (book == null) {
            throw new BookNotFoundException("Book not found!");
        }

        // If the book is already borrowed, throw an exception.
        if (book.isBorrowed()) {
            throw new BookAlreadyBorrowedException("Book was already borrowed!");
        }

        // Attempt to notify the user with the book's reviews.
        // This step is optional, so even if it fails, the book should still be returned.
        try {
            notifyUserWithBookReviews(ISBN, userId);
        } catch (Exception e) {
            System.out.println("Notification failed!");
        }

        // Return the retrieved book.
        return book;
    }
}

