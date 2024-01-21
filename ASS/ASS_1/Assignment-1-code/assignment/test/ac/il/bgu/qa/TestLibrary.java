package ac.il.bgu.qa;

// Import necessary libraries

/// Import services
import ac.il.bgu.qa.services.DatabaseService;
import ac.il.bgu.qa.services.NotificationService;
import ac.il.bgu.qa.services.ReviewService;

/// Import errors
import ac.il.bgu.qa.errors.*;

/// Import JUnit & Mockito necessary methods
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

// TestLibrary class that hold comprehensive tests for library
public class TestLibrary {

    private DatabaseService mockDatabaseService;
    private ReviewService mockReviewService;
    private NotificationService mockNotificationService;

    private Library library;
    private User testUser;
    private Book testBook;

    private Library test_library = null;

    @BeforeAll
    static void setupAll(){
        System.out.println("Initializing resources for all the tests");
    }


    @BeforeEach
    void setUp(){
        // Setup mocks in order to mock database ,reviews and notification services behaviours
        mockDatabaseService = mock(DatabaseService.class);
        mockReviewService = mock(ReviewService.class);
        mockNotificationService = mock(NotificationService.class);

        // Setup Library, User and Book objects for tests
        library = new Library(mockDatabaseService, mockReviewService);

        testUser = new User("Joseph McUser", "101244568214", mockNotificationService);
        testBook = new Book("9780132350884", "Clean Code: A Handbook of Agile Software Craftsmanship", "Robert C. Martin");
    }


    @Test
    void GivenNewBook_WhenAddBook_ThenBookAddedSuccessfully(){
        // setup desired mock behavior that indicated that testBook isn't in the database
        when(mockDatabaseService.getBookByISBN(testBook.getISBN())).thenReturn(null); // book not in the database yet, thus return null

        // verify that 'addBook()' method doesn't throw exception
        assertDoesNotThrow(() -> library.addBook(testBook));

        // verify that 'addBook()' method of the database services was called only once & the method called with testBook
        verify(mockDatabaseService, times(1)).addBook(testBook.getISBN(), testBook);
    }

    @Test
    void GivenExistingBook_WhenAddBook_TheBookAlreadyExistsException(){
        // setup desired mock behavior that indicated that book already existing in the database
        when(mockDatabaseService.getBookByISBN(testBook.getISBN())).thenReturn(testBook);

        // extract the exception when trying to add book that already exist in the database
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(testBook));

        // verify that the method indeed throws exception for already existing book by exception message
        assertEquals("Book already exists.", exception.getMessage());
    }

    @Test
    void GivenNewUser_WhenRegisterUser_TheUserRegisteredSuccessefully(){
        // setup desired mock behavior that indicated that testUser isn't in the database
        when(mockDatabaseService.getUserById(testUser.getId())).thenReturn(null); // user not in the database yet, thus return null

        // verify that registerUser() method doesn't throw exception
        assertDoesNotThrow(() -> library.registerUser(testUser));

        // verify that registerUser() method of the database called once & the method called with testUser
        verify(mockDatabaseService, times(1)).registerUser(testUser.getId(), testUser);
    }


    @Test
    void GivenExistingUser_WhenRegisterUser_ThenUserAlreadyExistsException() {
        // setup desired mock behavior that indicated that testUser already exists in the database
        when(mockDatabaseService.getUserById(testUser.getId())).thenReturn(testUser);

        // extract the exception when trying to register user that already registered in the database
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.registerUser(testUser));

        // verify that the method indeed throws exception for already registered user & registerUser() database method was never called
        assertEquals("User already exists.", exception.getMessage());
        verify(mockDatabaseService, never()).registerUser(any(), any());
    }


    @Test
    void GivenAvailableBookAndUser_WhenBorrowBook_ThenBookBorrowedSuccessfully() {
        when(mockDatabaseService.getBookByISBN(testBook.getISBN())).thenReturn(testBook);
        when(mockDatabaseService.getUserById(testUser.getId())).thenReturn(testUser);

        assertDoesNotThrow(() -> library.borrowBook(testBook.getISBN(), testUser.getId()));
        verify(mockDatabaseService, times(1)).borrowBook(testBook.getISBN(), testUser.getId());
    }

    @Test
    void GivenBookNotFound_WhenBorrowBook_ThenBookNotFoundException() {
        when(mockDatabaseService.getBookByISBN(testBook.getISBN())).thenReturn(null);

        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> library.borrowBook(testBook.getISBN(), testUser.getId()));
        assertEquals("Book not found!", exception.getMessage());
        //verify(testBook, never()).borrow();
        verify(mockDatabaseService, never()).borrowBook(any(), any());
    }

    @Test
    void GivenBorrowedBook_WhenReturnBook_ThenBookReturnedSuccessfully() {
        when(mockDatabaseService.getBookByISBN(testBook.getISBN())).thenReturn(testBook);

        testBook.borrow(); // Simulate book being borrowed

        assertDoesNotThrow(() -> library.returnBook(testBook.getISBN()));
        //verify(testBook, times(1)).returnBook();
        verify(mockDatabaseService, times(1)).returnBook(testBook.getISBN());
    }

    @Test
    void GivenBookNotFound_WhenReturnBook_ThenBookNotFoundException() {
        when(mockDatabaseService.getBookByISBN(testBook.getISBN())).thenReturn(null);

        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> library.returnBook(testBook.getISBN()));
        assertEquals("Book not found!", exception.getMessage());
        verify(mockDatabaseService, never()).returnBook(any());
    }

    //TODO : Add test for verifying exceptions when adding User&Book instances to Library instance with wrong formats
}

