package ac.il.bgu.qa;

import ac.il.bgu.qa.services.DatabaseService;
import ac.il.bgu.qa.services.NotificationService;
import ac.il.bgu.qa.services.ReviewService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestLibrary2{
    @Mock
    private Book mockBook;
    @Mock
    private Library mockLibrary;
    @Mock
    private User mockUser;

    @Mock
    private DatabaseService mockDatabaseService;
    @Mock
    private NotificationService mockNotificationService;

    @Mock
    private ReviewService mockReviewService;

    @BeforeEach
    public void init(){MockitoAnnotations.openMocks(this);}

    /**
     * Book Tests
     */
        @Test
        public void givenNewMockBook_whenGettersCalled_ThenReturnBookMetaInformation(){
        when(mockBook.getAuthor()).thenReturn("David");
        when(mockBook.getISBN()).thenReturn("55511");
        when(mockBook.getTitle()).thenReturn("The Islands");

        String autor = mockBook.getAuthor();
        String ISBN= mockBook.getISBN();
        String title=mockBook.getTitle();

        verify(mockBook).getAuthor();
        verify(mockBook).getISBN();
        verify(mockBook).getTitle();

        assertEquals(autor,"David");
        assertEquals(ISBN,"55511");
        assertEquals(title,"The Islands");
     }
    @Test
    public void givenNewRealBook_whenGettersCalled_ThenReturnBookMetaInformation(){
        Book book=new Book("55511","The Islands","David");
        String autor = book.getAuthor();
        String ISBN= book.getISBN();
        String title=book.getTitle();

        assertEquals(autor,"David");
        assertEquals(ISBN,"55511");
        assertEquals(title,"The Islands");
    }
     @Test
    public void givenABook_whenBorrowed_ThenReturnBorrowed(){
        when(mockBook.isBorrowed()).thenReturn(true);
        boolean flag= mockBook.isBorrowed();
        verify(mockBook).isBorrowed();
         assertTrue(flag);

     }
    @Test
    public void givenARealBook_whenBorrowed_CheckDifferentBorrowStates(){
        Book book=new Book("55511","The Islands","David");
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
    public void GivenNewUser_WhenGettersCalled_theReturnInformation(){
        User user=new User("avi","123456",mockNotificationService);
        assertEquals(user.getId(),"123456");
         assertEquals(user.getName(),"avi");
         assertSame(user.getNotificationService(),mockNotificationService);
     }
    @Test
    public void GivenNewUser_WhenSendNotificationCalled_thenVerifyNotificationSent(){
        User user=new User("avi","123456",mockNotificationService);
        user.sendNotification(null);
        verify(mockNotificationService).notifyUser("123456",null);
    }
    /**
     * Library AddBook Tests
     */

   @Test
   public void GivenNewLibrary_WhenAddNewBook_PassAllChecks(){
       Library library=new Library(mockDatabaseService,mockReviewService);
       //Next lines are for returning all the needed mock Strings to pass all checks
       when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
       when(mockBook.getTitle()).thenReturn("The islands");
       when(mockBook.getAuthor()).thenReturn("David The Third");
       when(mockBook.isBorrowed()).thenReturn(false);
       when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(null);

       library.addBook(mockBook);

       verify(mockBook,times(4)).getISBN(); //The Method gets called 4 times in the Library.AddBook Method
       verify(mockBook).getAuthor();
       verify(mockBook,times(2)).getTitle(); //The Method gets called 2 times in the Library.AddBook Method
       verify(mockBook).isBorrowed();
       verify(mockDatabaseService).addBook("978-3-16-148410-0",mockBook);
   }

    /**
     * Library AddBook Exceptions check
     */
    @Test
    public void GivenNewLibrary_WhenAddingNewBook_ActivateNullBookExceptions() {

        //Adding null Book Exception
        Library library = new Library(mockDatabaseService, mockReviewService);
        assertThrows(IllegalArgumentException.class, () -> library.addBook(null));
    }
    @Test
    public void GivenNewLibrary_WhenAddingNewBook_ActivateISBNExceptions() {
        Library library = new Library(mockDatabaseService, mockReviewService);

        //ISBN Exceptions
        when(mockBook.getISBN()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-000");
        assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));


    }
    @Test
    public void GivenNewLibrary_WhenAddingNewBook_ActivateTitleExceptions() {
        Library library = new Library(mockDatabaseService, mockReviewService);

        //Title Exceptions
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        when(mockBook.getTitle()).thenReturn("");
        assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
    }

    @Test
    public void GivenNewLibrary_WhenAddingNewBook_ActivateAuthorExceptions() {
        Library library = new Library(mockDatabaseService, mockReviewService);
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockBook.getAuthor()).thenReturn(null);
        assertThrows(IllegalArgumentException.class,()->library.addBook(mockBook));
        when(mockBook.getAuthor()).thenReturn("");
        assertThrows(IllegalArgumentException.class,()->library.addBook(mockBook));
        when(mockBook.getAuthor()).thenReturn("1234");
        assertThrows(IllegalArgumentException.class,()->library.addBook(mockBook));
        when(mockBook.getAuthor()).thenReturn("Da4vid");
        assertThrows(IllegalArgumentException.class,()->library.addBook(mockBook));
        when(mockBook.getAuthor()).thenReturn("da!@#$");
        assertThrows(IllegalArgumentException.class,()->library.addBook(mockBook));
        when(mockBook.getAuthor()).thenReturn("David--Third");
        assertThrows(IllegalArgumentException.class,()->library.addBook(mockBook));

    }

    @Test
    public void GivenNewLibrary_WhenAddingNewBook_ActivateIsBorrowedExceptions() {
        Library library = new Library(mockDatabaseService, mockReviewService);
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockBook.getAuthor()).thenReturn("David The third");
        when(mockBook.isBorrowed()).thenReturn(true);
        assertThrows(IllegalArgumentException.class,()->library.addBook(mockBook));
        }
        @Test
    public void GivenNewLibrary_WhenAddingNewBook_ActivateBookAlreadyInDatabaseExceptions() {
        Library library = new Library(mockDatabaseService, mockReviewService);
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockBook.getAuthor()).thenReturn("David The third");
        when(mockBook.isBorrowed()).thenReturn(false);
        when(mockDatabaseService.getBookByISBN("978-3-16-148410-0")).thenReturn(mockBook);
        assertThrows(IllegalArgumentException.class,()->library.addBook(mockBook));

    }

/**
 *Library RegisterUser Tests
 */
@Test
    public void GivenNewLibrary_WhenAddingNewUser_AddSuccessfully(){
    Library library=new Library(mockDatabaseService,mockReviewService);
    when(mockUser.getId()).thenReturn("206515744111");
    when(mockUser.getName()).thenReturn("david");
    when(mockUser.getNotificationService()).thenReturn(mockNotificationService);
    when(mockDatabaseService.getUserById("206515744111")).thenReturn(null);

    library.registerUser(mockUser);

    verify(mockUser,times(4)).getId();
    verify(mockUser,times(2)).getName();
    verify(mockUser).getNotificationService();
    verify(mockDatabaseService).getUserById("206515744111");
    verify(mockDatabaseService).registerUser("206515744111",mockUser);

}
}