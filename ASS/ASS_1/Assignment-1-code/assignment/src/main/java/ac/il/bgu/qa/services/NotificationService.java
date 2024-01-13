package ac.il.bgu.qa.services;

import ac.il.bgu.qa.errors.NotificationException;

/**
 * Provides an interface for services responsible for notifying users.
 */
public interface NotificationService {

    /**
     * Sends a notification message to a specific user.
     *
     * @param userId  The unique identifier of the user to be notified.
     * @param message The content of the notification message.
     * @throws NotificationException If there's an issue sending the notification.
     */
    void notifyUser(String userId, String message) throws NotificationException;
}
