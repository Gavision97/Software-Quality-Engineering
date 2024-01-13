package ac.il.bgu.qa.services;

import ac.il.bgu.qa.errors.ReviewException;

import java.util.List;

/**
 * Provides an interface for services that retrieve book reviews.
 */
public interface ReviewService {

    /**
     * Fetches the list of reviews for a book based on its ISBN.
     *
     * @param ISBN The International Standard Book Number (ISBN) of the book.
     * @return A list of reviews associated with the given book's ISBN.
     * @throws ReviewException If there's an issue fetching the reviews.
     */
    List<String> getReviewsForBook(String ISBN) throws ReviewException;

    /**
     * Closes the review service, performing any necessary cleanup operations.
     */
    void close();
}
