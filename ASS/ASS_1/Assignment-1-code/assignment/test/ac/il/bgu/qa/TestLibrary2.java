package ac.il.bgu.qa;

import ac.il.bgu.qa.errors.*;
import ac.il.bgu.qa.services.DatabaseService;
import ac.il.bgu.qa.services.NotificationService;
import ac.il.bgu.qa.services.ReviewService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestLibrary2 {
    @Mock
    Book mockBook;
    @Mock
    Library mockLibrary;
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

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Book Tests
     */
    @Test
    public void givenNewMockBook_whenGettersCalled_ThenReturnBookMetaInformation() {
        when(mockBook.getAuthor()).thenReturn("David");
        when(mockBook.getISBN()).thenReturn("55511");
        when(mockBook.getTitle()).thenReturn("The Islands");

        String autor = mockBook.getAuthor();
        String ISBN = mockBook.getISBN();
        String title = mockBook.getTitle();

        verify(mockBook).getAuthor();
        verify(mockBook).getISBN();
        verify(mockBook).getTitle();

        assertEquals(autor, "David");
        assertEquals(ISBN, "55511");
        assertEquals(title, "The Islands");
    }

    @Test
    public void givenNewRealBook_whenGettersCalled_ThenReturnBookMetaInformation() {
        Book book = new Book("55511", "The Islands", "David");
        String autor = book.getAuthor();
        String ISBN = book.getISBN();
        String title = book.getTitle();

        assertEquals(autor, "David");
        assertEquals(ISBN, "55511");
        assertEquals(title, "The Islands");
    }

    @Test
    public void givenABook_whenBorrowed_ThenReturnBorrowed() {
        when(mockBook.isBorrowed()).thenReturn(true);
        boolean flag = mockBook.isBorrowed();
        verify(mockBook).isBorrowed();
        assertTrue(flag);

    }

    @Test
    public void givenARealBook_whenBorrowed_CheckDifferentBorrowStates() {
        Book book = new Book("55511", "The Islands", "David");
        assertFalse(book.isBorrowed());
        book.borrow();
        assertTrue(book.isBorrowed());
        assertThrows(IllegalStateException.class, () -> {
            book.borrow();
        });
        book.returnBook();
        assertFalse(book.isBorrowed());
        assertThrows(IllegalStateException.class, () -> {
            book.returnBook();
        });
    }

    /**
     * User Tests
     */
    @Test
    public void GivenNewUser_WhenGettersCalled_theReturnInformation() {
        User user = new User("avi", "123456", mockNotificationService);
        assertEquals(user.getId(), "123456");
        assertEquals(user.getName(), "avi");
        assertSame(user.getNotificationService(), mockNotificationService);
    }

    @Test
    public void GivenNewUser_WhenSendNotificationCalled_thenVerifyNotificationSent() {
        User user = new User("avi", "123456", mockNotificationService);
        user.sendNotification(null);
        verify(mockNotificationService).notifyUser("123456", null);
    }

    /**
     * Library AddBook Tests
     */
    @Test
    public void GivenNewLibrary_WhenAddNewBook_PassAllChecks() {
        Library library = new Library(mockDatabaseService, mockReviewService);
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
        Library library = new Library(mockDatabaseService, mockReviewService);
        IllegalArgumentException exception= assertThrows(IllegalArgumentException.class, () -> library.addBook(null));
        assertEquals("Invalid book.", exception.getMessage());
    }

    @Test
    public void GivenNewLibrary_WhenAddingNewBook_TriggerISBNExceptions() {
        Library library = new Library(mockDatabaseService, mockReviewService);

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
        Library library = new Library(mockDatabaseService, mockReviewService);

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
        Library library = new Library(mockDatabaseService, mockReviewService);
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
        Library library = new Library(mockDatabaseService, mockReviewService);
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockBook.getAuthor()).thenReturn("David The third");
        when(mockBook.isBorrowed()).thenReturn(true);
        IllegalArgumentException exception= assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        assertEquals("Book with invalid borrowed state.",exception.getMessage());

    }

    @Test
    public void GivenNewLibrary_WhenAddingNewBook_TriggerBookAlreadyInDatabaseExceptions() {
        Library library = new Library(mockDatabaseService, mockReviewService);
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
        Library library = new Library(mockDatabaseService, mockReviewService);
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
        Library library = new Library(mockDatabaseService, mockReviewService);
       IllegalArgumentException exception= assertThrows(IllegalArgumentException.class, () -> library.registerUser(null));
       assertEquals("Invalid user.",exception.getMessage());
    }

    @Test
    public void GivenNewLibrary_WhenAddingNewUser_TriggerBadUserIDException() {
        Library library = new Library(mockDatabaseService, mockReviewService);
        when(mockUser.getId()).thenReturn(null);
        IllegalArgumentException exception= assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));
        assertThrows(IllegalArgumentException.class, () -> library.registerUser(null));

        when(mockUser.getId()).thenReturn("2A");
        assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));

    }

    @Test
    public void GivenNewLibrary_WhenAddingNewUser_TriggerBadUserNameException() {
        Library library = new Library(mockDatabaseService, mockReviewService);
        when(mockUser.getId()).thenReturn("1206515744111");
        when(mockUser.getName()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));
        when(mockUser.getName()).thenReturn("");
        assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));
    }

    @Test
    public void GivenNewLibrary_WhenAddingNewUser_TriggerBadUserNotificationServiceException() {
        Library library = new Library(mockDatabaseService, mockReviewService);
        when(mockUser.getId()).thenReturn("1206515744111");
        when(mockUser.getName()).thenReturn("David");
        when(mockUser.getNotificationService()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));

    }

    @Test
    public void GivenNewLibrary_WhenAddingNewUser_TriggerUserAlreadyInDatabaseException() {
        Library library = new Library(mockDatabaseService, mockReviewService);
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
        Library library = new Library(mockDatabaseService, mockReviewService);
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
        Library library = new Library(mockDatabaseService, mockReviewService);
        when(mockBook.getISBN()).thenReturn("a512gasc");
        when(mockUser.getId()).thenReturn("206515744111");
        assertThrows(IllegalArgumentException.class, () -> library.borrowBook(mockBook.getISBN(), mockUser.getId()));
    }

    @Test
    public void GivenLibrary_WhenBorrowBook_TriggerBookNotInDatabaseException() {
        Library library = new Library(mockDatabaseService, mockReviewService);
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(null);
        assertThrows(BookNotFoundException.class, () -> library.borrowBook(mockBook.getISBN(), mockUser.getId()));
    }

    @Test
    public void GivenLibrary_WhenBorrowBook_TriggerBadUserIDException() {
        Library library = new Library(mockDatabaseService, mockReviewService);
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockUser.getId()).thenReturn("20651574411441");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        assertThrows(IllegalArgumentException.class, () -> library.borrowBook(mockBook.getISBN(), mockUser.getId()));
    }

    @Test
    public void GivenLibrary_WhenBorrowBook_TriggerUserNotRegisteredException() {
        Library library = new Library(mockDatabaseService, mockReviewService);
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(null);
        assertThrows(UserNotRegisteredException.class, () -> library.borrowBook(mockBook.getISBN(), mockUser.getId()));
    }

    @Test
    public void GivenLibrary_WhenBorrowBook_TriggerBookAlreadyBorrowedException() {
        Library library = new Library(mockDatabaseService, mockReviewService);
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
        Library library = new Library(mockDatabaseService, mockReviewService);
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.isBorrowed()).thenReturn(true);
        when(mockDatabaseService.getBookByISBN("978-3-16-148410-0")).thenReturn(mockBook);
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);

        library.returnBook("978-3-16-148410-0");
        verify(mockDatabaseService).returnBook("978-3-16-148410-0");
    }

    @Test
    public void GivenLibrary_WhenReturnBook_TriggerBadISBNException() {
        Library library = new Library(mockDatabaseService, mockReviewService);
        when(mockBook.getISBN()).thenReturn("978-3-16-148410");
        assertThrows(IllegalArgumentException.class, () -> library.returnBook("978-3-16-148410"));

    }

    @Test
    public void GivenLibrary_WhenReturnBook_TriggerBookNotFoundException() {
        Library library = new Library(mockDatabaseService, mockReviewService);
        when(mockDatabaseService.getBookByISBN(any())).thenReturn(null);
        assertThrows(BookNotFoundException.class, () -> library.returnBook("978-3-16-148410-0"));
    }

    @Test
    public void GivenLibrary_WhenReturnBook_TriggerBookNotBorrowedException() {
        Library library = new Library(mockDatabaseService, mockReviewService);
        when(mockDatabaseService.getBookByISBN(any())).thenReturn(mockBook);
        when(mockBook.isBorrowed()).thenReturn(false);
        assertThrows(BookNotBorrowedException.class, () -> library.returnBook("978-3-16-148410-0"));
    }

    /**
     * Library NotifyUserWithBookReviews Tests
     */
    @Test
    public void GivenLibrary_WhenNotifyUserWithBookReviewCalled_NotifySuccessfully() {
        Library library=new Library(mockDatabaseService,mockReviewService);
        spyReviews=new ArrayList<>();
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
        Library library=new Library(mockDatabaseService,mockReviewService);
        assertThrows(IllegalArgumentException.class,()->library.notifyUserWithBookReviews("1243666","206515744111"));
    }
    @Test
    public void GivenLibrary_WhenNotifyUserWithBookReviewCalled_TriggerBadUserIDException() {
        Library library=new Library(mockDatabaseService,mockReviewService);
        assertThrows(IllegalArgumentException.class,()->library.notifyUserWithBookReviews("978-3-16-148410-0","206511"));
        assertThrows(IllegalArgumentException.class,()->library.notifyUserWithBookReviews("978-3-16-148410-0",null));

    }
    @Test
    public void GivenLibrary_WhenNotifyUserWithBookReviewCalled_TriggerBookNotFoundException() {
        Library library=new Library(mockDatabaseService,mockReviewService);
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(null);
       assertThrows(BookNotFoundException.class,()->library.notifyUserWithBookReviews("978-3-16-148410-0","206515744111"));
    }
    @Test
    public void GivenLibrary_WhenNotifyUserWithBookReviewCalled_TriggerUserNotRegisteredException() {
        Library library=new Library(mockDatabaseService,mockReviewService);
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(null);

        assertThrows(UserNotRegisteredException.class,()->library.notifyUserWithBookReviews("978-3-16-148410-0","206515744111"));
    }
    @Test
    public void GivenLibrary_WhenNotifyUserWithBookReviewCalled_TriggerNoReviewsFoundException() {
        Library library = new Library(mockDatabaseService, mockReviewService);
        spyReviews=new ArrayList<>();
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
        Library library = new Library(mockDatabaseService, mockReviewService);
        spyReviews = new ArrayList<>();
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);
        when(mockReviewService.getReviewsForBook(any())).thenThrow(ReviewServiceUnavailableException.class);
        assertThrows(ReviewServiceUnavailableException.class,()->library.notifyUserWithBookReviews("978-3-16-148410-0","206515744111"));
    }
    @Test
    public void GivenLibrary_WhenNotifyUserWithBookReviewCalled_TriggerNotificationException() {
        Library library = new Library(mockDatabaseService, mockReviewService);
        spyReviews = new ArrayList<>();
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