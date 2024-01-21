package ac.il.bgu.qa;

import ac.il.bgu.qa.errors.NotificationException;
import ac.il.bgu.qa.services.NotificationService;

/**
 * Represents a user of the library.
 */
public class User {

    // The name of the user.
    private final String name;
    // The unique identifier for the user.
    private final String id;
    // The service responsible for sending notifications to users.
    private final NotificationService notificationService;

    /**
     * Constructs a new User object.
     *
     * @param name               The name of the user.
     * @param id                 The unique identifier for the user.
     * @param notificationService The service responsible for sending notifications to users.
     */
    public User(String name, String id, NotificationService notificationService) {
        this.name = name;
        this.id = id;
        this.notificationService = notificationService;
    }

    // Getter methods

    /**
     * Retrieves the name of the user.
     *
     * @return The name of the user.
     */
    public String getName() {
        return name;
    }


    /**
     * Retrieves the unique identifier of the user.
     *
     * @return The user's Id.
     */
    public String getId() {
        return id;
    }

    /**
     * Retrieves the notification service associated with the user.
     *
     * @return The user's NotificationService instance.
     */
    public NotificationService getNotificationService() {
        return notificationService;
    }

    /**
     * Sends a notification message to this user.
     *
     * @param message The content of the notification.
     * @throws NotificationException If there's an error while sending the notification.
     */
    public void sendNotification(String message) throws NotificationException {
        notificationService.notifyUser(id, message);
    }

}

