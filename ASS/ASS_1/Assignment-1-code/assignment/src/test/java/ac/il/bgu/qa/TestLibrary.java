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

public class TestLibrary {
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
        verifyNoInteractions(mockDatabaseService);
    }

    @Test
    public void GivenNewLibrary_WhenAddingNewBook_TriggerISBNExceptionsByNullISBN() {

        //ISBN Exceptions
        when(mockBook.getISBN()).thenReturn(null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        assertEquals("Invalid ISBN.", exception.getMessage());
        verifyNoInteractions(mockDatabaseService);




    }
    @Test
    public void GivenInvalidBookISBN_WhenAddingNewBook_TriggerISBNExceptionsByISBNLength(){
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-000");
        IllegalArgumentException exception =assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        assertEquals("Invalid ISBN.", exception.getMessage());
        verifyNoInteractions(mockDatabaseService);

    }

    @Test
    public void GivenInvalidBookISBN_WhenAddingNewBook_TriggerISBNExceptionsByNotAllDigits(){
        when(mockBook.getISBN()).thenReturn("97!-3-1D-14%4105");
        IllegalArgumentException exception =assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        assertEquals("Invalid ISBN.", exception.getMessage());
        verifyNoInteractions(mockDatabaseService);
    }

    @Test
    public void GivenBookTitleIsNull_WhenAddingNewBook_TriggerTitleExceptionsByNullTitle() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn(null);


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        assertEquals("Invalid title.",exception.getMessage());
        verifyNoInteractions(mockDatabaseService);
    }

    @Test
    public void GivenInvalidBookTitle_WhenAddingNewBook_TriggerTitleExceptionsByEmptyTitle() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("");

        IllegalArgumentException exception=assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));

        assertEquals("Invalid title.",exception.getMessage());
        verifyNoInteractions(mockDatabaseService);
    }

    @Test
    public void GivenInvalidAuthor_WhenAddingNewBook_TriggerAuthorExceptionsByNullAuthor() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockBook.getAuthor()).thenReturn(null);

        IllegalArgumentException exception= assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        assertEquals("Invalid author.",exception.getMessage());
        verifyNoInteractions(mockDatabaseService);


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
    public void GivenInvalidAuthor_WhenAddingNewBook_TriggerAuthorExceptionsByEmptyAuthor() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockBook.getAuthor()).thenReturn("");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));

        assertEquals("Invalid author.", exception.getMessage());
        verifyNoInteractions(mockDatabaseService);
    }

    @Test
    public void GivenIvalidAuthor_WhenAddingNewBook_TriggerAuthorExceptionsByDoesntStartOrEndWithChar() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockBook.getAuthor()).thenReturn("5vid");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));

        assertEquals("Invalid author.", exception.getMessage());
        verifyNoInteractions(mockDatabaseService);


        when(mockBook.getAuthor()).thenReturn("davi5");

        exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));

        assertEquals("Invalid author.", exception.getMessage());
        verifyNoInteractions(mockDatabaseService);
    }

    @Test
    public void GivenInvalidAuthor_WhenAddingNewBook_TriggerAuthorExceptionsByHaveOtherThanCharInAuthor() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockBook.getAuthor()).thenReturn("da7id");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));

        assertEquals("Invalid author.", exception.getMessage());
        verifyNoInteractions(mockDatabaseService);

        when(mockBook.getAuthor()).thenReturn("d!id");

        exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));

        assertEquals("Invalid author.", exception.getMessage());
        verifyNoInteractions(mockDatabaseService);
    }

    @Test
    public void GivenInvalidAuthor_WhenAddingNewBook_TriggerAuthorExceptionsByHaveMakafTwiceInARow() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockBook.getAuthor()).thenReturn("dav--id");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));

        assertEquals("Invalid author.", exception.getMessage());
        verifyNoInteractions(mockDatabaseService);
    }

    @Test
    public void GivenBookWithInvalidBorrowedState_WhenAddingNewBook_TriggerIsBorrowedExceptions() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockBook.getAuthor()).thenReturn("David The third");
        when(mockBook.isBorrowed()).thenReturn(true);

        IllegalArgumentException exception= assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));

        assertEquals("Book with invalid borrowed state.",exception.getMessage());
        verifyNoInteractions(mockDatabaseService);
    }

    @Test
    public void GivenBookAlreadyExist_WhenAddingNewBook_TriggerBookAlreadyInDatabaseExceptions() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockBook.getAuthor()).thenReturn("David The third");
        when(mockBook.isBorrowed()).thenReturn(false);
        when(mockDatabaseService.getBookByISBN("978-3-16-148410-0")).thenReturn(mockBook);

        IllegalArgumentException exception= assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));

        assertEquals("Book already exists.",exception.getMessage());
        verify(mockDatabaseService,times(0)).addBook(mockBook.getISBN(),mockBook);
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
        verify(mockDatabaseService,times(0)).registerUser(any(),any());
    }

    @Test
    public void GivenUserIdIsNull_WhenAddingNewUser_TriggerBadUserIDExceptionByNullID() {
        when(mockUser.getId()).thenReturn(null);

        IllegalArgumentException exception= assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));

        assertEquals("Invalid user Id.",exception.getMessage());
        verify(mockDatabaseService,times(0)).registerUser(any(),any());
    }

    @Test
    public void GivenInvalidUserId_WhenAddingNewUser_TriggerBadUserIDExceptionByWrongLengthID() {
        when(mockUser.getId()).thenReturn("155555");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));

        assertEquals("Invalid user Id.",exception.getMessage());
        verify(mockDatabaseService,times(0)).registerUser(any(),any());

        when(mockUser.getId()).thenReturn("1552222222222222222222222255");

        exception = assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));

        assertEquals("Invalid user Id.",exception.getMessage());
        verify(mockDatabaseService,times(0)).registerUser(any(),any());
    }

    @Test
    public void GivenInvalidUserName_WhenAddingNewUser_TriggerBadUserNameExceptionByNullName() {
        when(mockUser.getId()).thenReturn("120651574411");
        when(mockUser.getName()).thenReturn(null);

        IllegalArgumentException exception=assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));

        assertEquals("Invalid user name.",exception.getMessage());
        verify(mockDatabaseService,times(0)).registerUser(any(),any());
    }

    @Test
    public void GivenInvalidUserName_WhenAddingNewUser_TriggerBadUserNameExceptionByEmptyName() {
        when(mockUser.getId()).thenReturn("120651574411");
        when(mockUser.getName()).thenReturn("");

        IllegalArgumentException exception=assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));
        assertEquals("Invalid user name.",exception.getMessage());
        verify(mockDatabaseService,times(0)).registerUser(any(),any());

    }

    @Test
    public void GivenInvalidNotificationService_WhenAddingNewUser_TriggerBadUserNotificationServiceExceptionByNullNotifictionService() {
        when(mockUser.getId()).thenReturn("120651574411");
        when(mockUser.getName()).thenReturn("David");
        when(mockUser.getNotificationService()).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));

        assertEquals("Invalid notification service.", exception.getMessage());
        verify(mockDatabaseService, times(0)).registerUser(any(), any());
    }

    @Test
    public void GivenUserAlreadyExist_WhenAddingNewUser_TriggerUserAlreadyInDatabaseException() {
        when(mockUser.getId()).thenReturn("120651574411");
        when(mockUser.getName()).thenReturn("David");
        when(mockUser.getNotificationService()).thenReturn(mockNotificationService);
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);

        IllegalArgumentException exception=assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));

        assertEquals("User already exists.",exception.getMessage());
        verify(mockDatabaseService,times(0)).registerUser(any(),any());
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
    public void GivenBookISBNisNull_WhenBorrowBook_TriggerBadISBNExceptionByNullException() {
        when(mockBook.getISBN()).thenReturn(null);
        when(mockUser.getId()).thenReturn("206515744111");

        IllegalArgumentException exception=assertThrows(IllegalArgumentException.class, () -> library.borrowBook(mockBook.getISBN(), mockUser.getId()));

        assertEquals("Invalid ISBN.",exception.getMessage());
        verify(mockDatabaseService,times(0)).borrowBook(any(),any());
    }

    @Test
    public void GivenInvalidBookISBNLength_WhenBorrowBook_TriggerBadISBNExceptionByLengthOrNotOnlyDigitsException() {
        when(mockBook.getISBN()).thenReturn("2");
        when(mockUser.getId()).thenReturn("206515744111");

        IllegalArgumentException exception=assertThrows(IllegalArgumentException.class, () -> library.borrowBook(mockBook.getISBN(), mockUser.getId()));

        assertEquals("Invalid ISBN.",exception.getMessage());
        verify(mockDatabaseService,times(0)).borrowBook(any(),any());

        when(mockBook.getISBN()).thenReturn("A23456789011");

        exception=assertThrows(IllegalArgumentException.class, () -> library.borrowBook(mockBook.getISBN(), mockUser.getId()));

        assertEquals("Invalid ISBN.",exception.getMessage());
        verify(mockDatabaseService,times(0)).borrowBook(any(),any());
    }

    @Test
    public void GivenBookNotInDatabase_WhenBorrowBook_TriggerBookNotFoundInDatabaseException() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(null);

        BookNotFoundException exception= assertThrows(BookNotFoundException.class, () -> library.borrowBook(mockBook.getISBN(), mockUser.getId()));

        assertEquals("Book not found!",exception.getMessage());
        verify(mockDatabaseService,times(0)).borrowBook(any(),any());
    }

    @Test
    public void GivenUserIdIsNull_WhenBorrowBook_TriggerBadUserIDExceptionByNullID() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockUser.getId()).thenReturn(null);
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);

        IllegalArgumentException exception= assertThrows(IllegalArgumentException.class, () -> library.borrowBook(mockBook.getISBN(), mockUser.getId()));

        assertEquals("Invalid user Id.",exception.getMessage());
        verify(mockDatabaseService,times(0)).borrowBook(any(),any());
    }

    @Test
    public void GivenInvalidUserId_WhenBorrowBook_TriggerBadUserIDExceptionByNotAllDigitsID() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockUser.getId()).thenReturn("55A671245621");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);

        IllegalArgumentException exception= assertThrows(IllegalArgumentException.class, () -> library.borrowBook(mockBook.getISBN(), mockUser.getId()));

        assertEquals("Invalid user Id.",exception.getMessage());
        verify(mockDatabaseService,times(0)).borrowBook(any(),any());
    }

    @Test
    public void GivenNotRegisteredUser_WhenBorrowBook_TriggerUserNotRegisteredException() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(null);

        UserNotRegisteredException exception= assertThrows(UserNotRegisteredException.class, () -> library.borrowBook(mockBook.getISBN(), mockUser.getId()));

        assertEquals("User not found!",exception.getMessage());
        verify(mockDatabaseService,times(0)).borrowBook(any(),any());
    }

    @Test
    public void GivenBorrowedBook_WhenBorrowBook_TriggerBookAlreadyBorrowedException() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);
        when(mockBook.isBorrowed()).thenReturn(true);

        BookAlreadyBorrowedException exception= assertThrows(BookAlreadyBorrowedException.class, () -> library.borrowBook(mockBook.getISBN(), mockUser.getId()));

        assertEquals("Book is already borrowed!",exception.getMessage());
        verify(mockDatabaseService,times(0)).borrowBook(any(),any());
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
    public void GivenBookISBNIsNull_WhenReturnBook_TriggerBadISBNExceptionByNullISBN() {
        when(mockBook.getISBN()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> library.returnBook(mockBook.getISBN()));
        IllegalArgumentException exception=assertThrows(IllegalArgumentException.class, () -> library.returnBook(mockBook.getISBN()));
        assertEquals("Invalid ISBN.",exception.getMessage());
        verify(mockDatabaseService,times(0)).returnBook(any());
    }

    @Test
    public void GivenBookNotInDatabase_WhenReturnBook_TriggerBookNotFoundExceptionByNullBook() {
        when(mockDatabaseService.getBookByISBN(any())).thenReturn(null);
        BookNotFoundException exception= assertThrows(BookNotFoundException.class, () -> library.returnBook("978-3-16-148410-0"));

        assertEquals("Book not found!",exception.getMessage());
        verify(mockDatabaseService,times(0)).returnBook(any());
    }


    @Test
    public void GivenUnborrowedBook_WhenReturnBook_TriggerBookNotBorrowedExceptionByBorrowedBook() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockDatabaseService.getBookByISBN(any())).thenReturn(mockBook);
        when(mockBook.isBorrowed()).thenReturn(false);

        BookNotBorrowedException exception= assertThrows(BookNotBorrowedException.class, () -> library.returnBook(mockBook.getISBN()));

        assertEquals("Book wasn't borrowed!",exception.getMessage());
        verify(mockDatabaseService,times(0)).returnBook(any());
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
    public void GivenIlligalArgumentException_WhenNotifyUserWithBookReviewCalled_TriggerBadISBNExceptionWhenISBNEmptyOrNull() {
        IllegalArgumentException exception= assertThrows(IllegalArgumentException.class,()->library.notifyUserWithBookReviews(null,"206515744111"));
        assertEquals("Invalid ISBN.",exception.getMessage());
        verify(mockUser,times(0)).sendNotification(any());

        exception= assertThrows(IllegalArgumentException.class,
                ()->library.notifyUserWithBookReviews("","206515744111"));

        assertEquals("Invalid ISBN.",exception.getMessage());
        verify(mockUser,times(0)).sendNotification(any());
    }

    @Test
    public void GivenNotifiedUserIdIsNull_WhenNotifyUserWithBookReviewCalled_TriggerBadUserIDExceptionByNull() {
        IllegalArgumentException exception=assertThrows(IllegalArgumentException.class,
                ()->library.notifyUserWithBookReviews("978-3-16-148410-0",null));

        assertEquals("Invalid user Id.",exception.getMessage());
        verify(mockUser,times(0)).sendNotification(any());
    }

    @Test
    public void GivenBookISBNIsNull_WhenNotifyUserWithBookReviewCalled_TriggerBookNotFoundExceptionByNullBook() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(null);

        BookNotFoundException exception= assertThrows(BookNotFoundException.class,
                ()->library.notifyUserWithBookReviews("978-3-16-148410-0","206515744111"));

        assertEquals("Book not found!",exception.getMessage());
        verify(mockUser,times(0)).sendNotification(any());
    }
    @Test
    public void GivenReturnedUserIdFromDatabaseServiceIsNull_WhenNotifyUserWithBookReviewCalled_TriggerUserNotRegisteredExceptionByNullUser() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(null);

        UserNotRegisteredException exception=  assertThrows(UserNotRegisteredException.class,
                ()->library.notifyUserWithBookReviews("978-3-16-148410-0","206515744111"));

        assertEquals("User not found!",exception.getMessage());
        verify(mockUser,times(0)).sendNotification(any());
    }
    @Test
    public void GivenGetReviewsForBookFromReviewServiceIsNull_WhenNotifyUserWithBookReviewCalled_TriggerNoReviewsFoundExceptionByNullReviews() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);
        when(mockReviewService.getReviewsForBook(any())).thenReturn(null);

        NoReviewsFoundException exception= assertThrows(NoReviewsFoundException.class,
                ()->library.notifyUserWithBookReviews("978-3-16-148410-0","206515744111"));

        assertEquals("No reviews found!",exception.getMessage());
        verify(mockUser,times(0)).sendNotification(any());
    }
    @Test
    public void GivenEmptyReviews_WhenNotifyUserWithBookReviewCalled_TriggerNoReviewsFoundExceptionByEmptyReviews() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);
        when(mockReviewService.getReviewsForBook(any())).thenReturn(spyReviews);

        NoReviewsFoundException exception=assertThrows(NoReviewsFoundException.
                class,()->library.notifyUserWithBookReviews("978-3-16-148410-0","206515744111"));

        assertEquals("No reviews found!",exception.getMessage());
        verify(mockUser,times(0)).sendNotification(any());
    }

    @Test
    public void GivenLibrary_WhenNotifyUserWithBookReviewCalled_TriggerReviewServiceUnavailableException() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);

        when(mockReviewService.getReviewsForBook(mockBook.getISBN()))
                .thenThrow(new ReviewServiceUnavailableException("Review service unavailable!"));
        ReviewServiceUnavailableException exception = assertThrows(ReviewServiceUnavailableException.class,
                () -> library.notifyUserWithBookReviews("978-3-16-148410-0", "206515744111"));
        assertEquals("Review service unavailable!", exception.getMessage());
        verify(mockUser,times(0)).sendNotification(any());
    }

    @Test
    public void GivenNotificationProblem_WhenNotifyUserWithBookReviewCalled_TriggerNotificationException() {
        spyReviews.add("Amazing Book");
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);
        when(mockReviewService.getReviewsForBook(any())).thenReturn(spyReviews);
        doThrow(NotificationException.class).when(mockUser).sendNotification(any());

        NotificationException exception= assertThrows(NotificationException.class,
                ()->library.notifyUserWithBookReviews("978-3-16-148410-0","206515744111"));

        assertEquals("Notification failed!",exception.getMessage());
        verify(mockUser,times(5)).sendNotification(any());
    }

    @Test
    public void GivenGetReviewsForBookCatchException_WhenNotifyUserWithBookReviews_TriggerReviewServiceUnavailableException(){
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("The Islands");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);
        when(mockReviewService.getReviewsForBook(any())).thenThrow(
                ReviewException.class
        );

        ReviewServiceUnavailableException exception = assertThrows(ReviewServiceUnavailableException.class,
                () -> library.notifyUserWithBookReviews(mockBook.getISBN(), mockUser.getId()));

        assertEquals("Review service unavailable!", exception.getMessage());
        verify(mockUser, times(0)).sendNotification(any());
    }

    @Test
    public void GivenInvalidBookISBN_WhenGetBookByISBN_TriggerIllegalArgumentException() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148%%%0-0");
        when(mockUser.getId()).thenReturn("206515744111");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                ()->library.getBookByISBN(mockBook.getISBN(), mockUser.getId()));

        assertEquals("Invalid ISBN.", exception.getMessage());
        verify(mockUser, times(0)).sendNotification(any());
    }

    @Test
    public void GivenUserIdIsNull_WhenGetBookByISBN_TriggerIllegalArgumentException() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockUser.getId()).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                ()->library.getBookByISBN(mockBook.getISBN(), mockUser.getId()));

        assertEquals("Invalid user Id.", exception.getMessage());
        verify(mockUser, times(0)).sendNotification(any());
    }

    @Test
    public void GivenUserIdIsInvalid_WhenGetBookByISBN_TriggerIllegalArgumentException() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockUser.getId()).thenReturn("qadsad15616500dad546ad5adsqeqe");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                ()->library.getBookByISBN(mockBook.getISBN(), mockUser.getId()));

        assertEquals("Invalid user Id.", exception.getMessage());
        verify(mockUser, times(0)).sendNotification(any());
    }

    @Test
    public void GivenBookIsNull_WhenGetBookByISBN_TriggerBookNotFoundException() {
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(null);

        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> library.getBookByISBN(mockBook.getISBN(), mockUser.getId())
        );

        assertEquals("Book not found!", exception.getMessage());
        verify(mockUser, times(0)).sendNotification(any());
    }

    @Test
    public void GivenBookIsAlreadyBorrowed_WhenGetBookByISBN_TriggerBookAlreadyBorrowedException(){
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        when(mockBook.isBorrowed()).thenReturn(true);

        BookAlreadyBorrowedException exception = assertThrows(BookAlreadyBorrowedException.class,
                ()-> library.getBookByISBN(mockBook.getISBN(), mockUser.getId())
        );

        assertEquals("Book was already borrowed!", exception.getMessage());
        verify(mockUser, times(0)).sendNotification(any());
    }

    @Test
    public void GivenAttemptToNotifyUserWithBookReview_WhenGetBookByISBN_NotificationFailedAndReturnBook() {
        spyReviews.add("Amazing Book!");
        spyReviews.add("Great Book! best book ever");

        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockBook.getTitle()).thenReturn("Abra Kadabra");
        when(mockUser.getId()).thenReturn("206515744111");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);
        when(mockReviewService.getReviewsForBook(mockBook.getISBN())).thenReturn(spyReviews);

        doThrow(NotificationException.class).when(mockUser).sendNotification(any());

        assertEquals(mockBook,library.getBookByISBN(mockBook.getISBN(), mockUser.getId()));
        assertEquals(2, spyReviews.size());
    }

}