package ac.il.bgu.qa;

import ac.il.bgu.qa.services.DatabaseService;
import ac.il.bgu.qa.services.NotificationService;
import ac.il.bgu.qa.services.ReviewService;
import ac.il.bgu.qa.errors.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

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
        public void givenNewBook_whenGettersCalled_ThenReturnBookMetaInformation(){
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
    public void givenABook_whenBorrowed_ThenReturnBorrowed(){
        when(mockBook.isBorrowed()).thenReturn(true);
        boolean flag= mockBook.isBorrowed();
        verify(mockBook).isBorrowed();
         assertTrue(flag);

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

    @Test
    public

}