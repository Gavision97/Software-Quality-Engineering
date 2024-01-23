package ac.il.bgu.qa;

import ac.il.bgu.qa.errors.*;
import ac.il.bgu.qa.services.DatabaseService;
import ac.il.bgu.qa.services.NotificationService;
import ac.il.bgu.qa.services.ReviewService;

import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestLibrary2 {
    @Mock
    Book mockBook;
    @Mock
    User mockUser;
    @Mock
     DatabaseService mockDatabaseService;
    @Mock
    NotificationService mockNotificationService;
    @Mock
    ReviewService mockReviewService;
    @Spy
    List<String> spyReviews;
    Library library;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks (this);
        library=new Library(mockDatabaseService,mockReviewService);
        spyReviews=new ArrayList<>();
    }

    /**
     * Library AddBook Tests
     */
    @Test
    public void GivenNewLibrary_WhenAddNewBook_PassAllChecks() {
        //Next lines are for returning all the needed mock Strings to pass all checks
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The islands");
        when(mockBook.getAuthor()).thenReturn("David The Third");
        when(mockBook.isBorrowed()).thenReturn(false);
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(null);

        library.addBook(mockBook);

        verify(mockBook, times(4)).getISBN(); //The Method gets called 4 times in the Library.AddBook Method
        verify(mockBook).getAuthor();
        verify(mockBook, times(2)).getTitle(); //The Method gets called 2 times in the Library.AddBook Method
        verify(mockBook).isBorrowed();
        verify(mockDatabaseService).addBook("978-3-16-148410-0", mockBook);
    }

    /**
     * Library AddBook Exceptions check
     */
    @Test
    public void GivenNewLibrary_WhenAddingNewBook_TriggerNullBookExceptions() {

        //Adding null Book Exception
        IllegalArgumentException exception= assertThrows(IllegalArgumentException.class, () -> library.addBook(null));
        assertEquals("Invalid book.", exception.getMessage());
    }

    @Test
    public void GivenNewLibrary_WhenAddingNewBook_TriggerISBNExceptions() {

        //ISBN Exceptions
        when(mockBook.getISBN()).thenReturn(null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        assertEquals("Invalid ISBN.", exception.getMessage());
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-000");
        exception =assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        assertEquals("Invalid ISBN.", exception.getMessage());


    }

    @Test
    public void GivenNewLibrary_WhenAddingNewBook_TriggerTitleExceptions() {

        //Title Exceptions
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn(null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        assertEquals("Invalid title.",exception.getMessage());
        when(mockBook.getTitle()).thenReturn("");
        exception=assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        assertEquals("Invalid title.",exception.getMessage());

    }

    @Test
    public void GivenNewLibrary_WhenAddingNewBook_TriggerAuthorExceptions() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockBook.getAuthor()).thenReturn(null);
        IllegalArgumentException exception= assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        assertEquals("Invalid author.",exception.getMessage());

        when(mockBook.getAuthor()).thenReturn("");
        exception= assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        assertEquals("Invalid author.",exception.getMessage());

        when(mockBook.getAuthor()).thenReturn("1234");
        exception= assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        assertEquals("Invalid author.",exception.getMessage());

        when(mockBook.getAuthor()).thenReturn("Da4vid");
        exception= assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        assertEquals("Invalid author.",exception.getMessage());

        when(mockBook.getAuthor()).thenReturn("da!@#$");
        exception= assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        assertEquals("Invalid author.",exception.getMessage());

        when(mockBook.getAuthor()).thenReturn("David--Third");
        exception= assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        assertEquals("Invalid author.",exception.getMessage());


    }

    @Test
    public void GivenNewLibrary_WhenAddingNewBook_TriggerIsBorrowedExceptions() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockBook.getAuthor()).thenReturn("David The third");
        when(mockBook.isBorrowed()).thenReturn(true);
        IllegalArgumentException exception= assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        assertEquals("Book with invalid borrowed state.",exception.getMessage());

    }

    @Test
    public void GivenNewLibrary_WhenAddingNewBook_TriggerBookAlreadyInDatabaseExceptions() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockBook.getAuthor()).thenReturn("David The third");
        when(mockBook.isBorrowed()).thenReturn(false);
        when(mockDatabaseService.getBookByISBN("978-3-16-148410-0")).thenReturn(mockBook);
        IllegalArgumentException exception= assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        assertEquals("Book already exists.",exception.getMessage());


    }

    /**
     * Library RegisterUser Tests
     */
    @Test
    public void GivenNewLibrary_WhenAddingNewUser_AddSuccessfully() {
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockUser.getName()).thenReturn("david");
        when(mockUser.getNotificationService()).thenReturn(mockNotificationService);
        when(mockDatabaseService.getUserById("206515744111")).thenReturn(null);

        library.registerUser(mockUser);

        verify(mockUser, times(4)).getId();
        verify(mockUser, times(2)).getName();
        verify(mockUser).getNotificationService();
        verify(mockDatabaseService).getUserById("206515744111");
        verify(mockDatabaseService).registerUser("206515744111", mockUser);

    }

    @Test
    public void GivenNewLibrary_WhenAddingNewUser_TriggerNullUserException() {
       IllegalArgumentException exception= assertThrows(IllegalArgumentException.class, () -> library.registerUser(null));
       assertEquals("Invalid user.",exception.getMessage());
    }

    @Test
    public void GivenNewLibrary_WhenAddingNewUser_TriggerBadUserIDException() {
        when(mockUser.getId()).thenReturn(null);
        IllegalArgumentException exception= assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));
        assertThrows(IllegalArgumentException.class, () -> library.registerUser(null));

        when(mockUser.getId()).thenReturn("2A");
        assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));

    }

    @Test
    public void GivenNewLibrary_WhenAddingNewUser_TriggerBadUserNameException() {
        when(mockUser.getId()).thenReturn("1206515744111");
        when(mockUser.getName()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));
        when(mockUser.getName()).thenReturn("");
        assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));
    }

    @Test
    public void GivenNewLibrary_WhenAddingNewUser_TriggerBadUserNotificationServiceException() {
        when(mockUser.getId()).thenReturn("1206515744111");
        when(mockUser.getName()).thenReturn("David");
        when(mockUser.getNotificationService()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));

    }

    @Test
    public void GivenNewLibrary_WhenAddingNewUser_TriggerUserAlreadyInDatabaseException() {
        when(mockUser.getId()).thenReturn("1206515744111");
        when(mockUser.getName()).thenReturn("David");
        when(mockUser.getNotificationService()).thenReturn(mockNotificationService);
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);
        assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));
    }

    /**
     * Library BorrowBook Tests
     */
    @Test
    public void GivenLibrary_WhenBorrowBook_BorrowBookSuccessfully() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);
        when(mockBook.isBorrowed()).thenReturn(false);

        library.borrowBook(mockBook.getISBN(), mockUser.getId());
        verify(mockDatabaseService).borrowBook(mockBook.getISBN(), mockUser.getId());

    }

    @Test
    public void GivenLibrary_WhenBorrowBook_TriggerBadISBNException() {
        when(mockBook.getISBN()).thenReturn("a512gasc");
        when(mockUser.getId()).thenReturn("206515744111");
        assertThrows(IllegalArgumentException.class, () -> library.borrowBook(mockBook.getISBN(), mockUser.getId()));
    }

    @Test
    public void GivenLibrary_WhenBorrowBook_TriggerBookNotInDatabaseException() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(null);
        assertThrows(BookNotFoundException.class, () -> library.borrowBook(mockBook.getISBN(), mockUser.getId()));
    }

    @Test
    public void GivenLibrary_WhenBorrowBook_TriggerBadUserIDException() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockUser.getId()).thenReturn("20651574411441");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        assertThrows(IllegalArgumentException.class, () -> library.borrowBook(mockBook.getISBN(), mockUser.getId()));
    }

    @Test
    public void GivenLibrary_WhenBorrowBook_TriggerUserNotRegisteredException() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(null);
        assertThrows(UserNotRegisteredException.class, () -> library.borrowBook(mockBook.getISBN(), mockUser.getId()));
    }

    @Test
    public void GivenLibrary_WhenBorrowBook_TriggerBookAlreadyBorrowedException() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);
        when(mockBook.isBorrowed()).thenReturn(true);
        assertThrows(BookAlreadyBorrowedException.class, () -> library.borrowBook(mockBook.getISBN(), mockUser.getId()));
    }

    /**
     * Library ReturnBook Tests
     */
    @Test
    public void GivenLibrary_WhenReturnBook_ReturnSuccessfully() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.isBorrowed()).thenReturn(true);
        when(mockDatabaseService.getBookByISBN("978-3-16-148410-0")).thenReturn(mockBook);
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);

        library.returnBook("978-3-16-148410-0");
        verify(mockDatabaseService).returnBook("978-3-16-148410-0");
    }

    @Test
    public void GivenLibrary_WhenReturnBook_TriggerBadISBNException() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410");
        assertThrows(IllegalArgumentException.class, () -> library.returnBook("978-3-16-148410"));

    }

    @Test
    public void GivenLibrary_WhenReturnBook_TriggerBookNotFoundException() {
        when(mockDatabaseService.getBookByISBN(any())).thenReturn(null);
        assertThrows(BookNotFoundException.class, () -> library.returnBook("978-3-16-148410-0"));
    }

    @Test
    public void GivenLibrary_WhenReturnBook_TriggerBookNotBorrowedException() {
        when(mockDatabaseService.getBookByISBN(any())).thenReturn(mockBook);
        when(mockBook.isBorrowed()).thenReturn(false);
        assertThrows(BookNotBorrowedException.class, () -> library.returnBook("978-3-16-148410-0"));
    }

    /**
     * Library NotifyUserWithBookReviews Tests
     */
    @Test
    public void GivenLibrary_WhenNotifyUserWithBookReviewCalled_NotifySuccessfully() {
        spyReviews.add("Amazing Book!");
        spyReviews.add("Great Book! best book ever");

        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);
        when(mockReviewService.getReviewsForBook(mockBook.getISBN())).thenReturn(spyReviews);

        library.notifyUserWithBookReviews(mockBook.getISBN(),mockUser.getId());
        verify(mockReviewService).close();
        verify(mockUser).sendNotification(any());
    }
    @Test
    public void GivenLibrary_WhenNotifyUserWithBookReviewCalled_TriggerBadISBNException() {
        assertThrows(IllegalArgumentException.class,()->library.notifyUserWithBookReviews("1243666","206515744111"));
    }
    @Test
    public void GivenLibrary_WhenNotifyUserWithBookReviewCalled_TriggerBadUserIDException() {
        assertThrows(IllegalArgumentException.class,()->library.notifyUserWithBookReviews("978-3-16-148410-0","206511"));
        assertThrows(IllegalArgumentException.class,()->library.notifyUserWithBookReviews("978-3-16-148410-0",null));

    }
    @Test
    public void GivenLibrary_WhenNotifyUserWithBookReviewCalled_TriggerBookNotFoundException() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(null);
       assertThrows(BookNotFoundException.class,()->library.notifyUserWithBookReviews("978-3-16-148410-0","206515744111"));
    }
    @Test
    public void GivenLibrary_WhenNotifyUserWithBookReviewCalled_TriggerUserNotRegisteredException() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(null);

        assertThrows(UserNotRegisteredException.class,()->library.notifyUserWithBookReviews("978-3-16-148410-0","206515744111"));
    }
    @Test
    public void GivenLibrary_WhenNotifyUserWithBookReviewCalled_TriggerNoReviewsFoundException() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);
        when(mockReviewService.getReviewsForBook(any())).thenReturn(null);
        assertThrows(NoReviewsFoundException.class,()->library.notifyUserWithBookReviews("978-3-16-148410-0","206515744111"));
        when(mockReviewService.getReviewsForBook(any())).thenReturn(spyReviews);
        assertThrows(NoReviewsFoundException.class,()->library.notifyUserWithBookReviews("978-3-16-148410-0","206515744111"));
    }
    @Test
    public void GivenLibrary_WhenNotifyUserWithBookReviewCalled_TriggerReviewServiceUnavailException() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);
        when(mockReviewService.getReviewsForBook(any())).thenThrow(ReviewServiceUnavailableException.class);
        assertThrows(ReviewServiceUnavailableException.class,()->library.notifyUserWithBookReviews("978-3-16-148410-0","206515744111"));
    }
    @Disabled
    public void GivenLibrary_WhenNotifyUserWithBookReviewCalled_TriggerNotificationException() {
        spyReviews.add("Amazing Book");

        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);
        when(mockReviewService.getReviewsForBook(any())).thenReturn(spyReviews);
        doThrow(NotificationException.class).when(mockUser).sendNotification(any());
        assertThrows(NotificationException.class,()->library.notifyUserWithBookReviews("978-3-16-148410-0","206515744111"));
    }
/**
 * Library GetBookByIsbn Tests
 */
}