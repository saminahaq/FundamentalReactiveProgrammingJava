package demo;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.channels.IllegalSelectorException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import Exception.BookException;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class BookServiceMockTest {

	@InjectMocks
	private BookService bserv;  //this service we need to test

	@Mock
	private BookInfoService bookInfoServ; //these are the object of the service class
	@Mock 
	private BookReviewService bookReviewServ;//these are the object of the service class


	@Test  //we are doing Mockito test  :Postive test
	void getBooksMocks()
	{

		//now calling the objects of the bookservice class through Mockito
		//For BookInfoService bookInfoServ
		Mockito.when(bookInfoServ.getBooks())
		.thenCallRealMethod();

		//For BookReviewService bookReviewServ : with input parameter used Mockito.anyLong()
		Mockito.when(bookReviewServ.getReview(Mockito .anyLong()))
		.thenCallRealMethod(); 

		var books = bserv.getBooks().log();

		StepVerifier.create(books)
		.expectNextCount(3)
		.verifyComplete();

	}


	@Test  //Test when there is an Error
	void getBooksMockOnerror() {

		Mockito.when(bookInfoServ.getBooks())
		.thenCallRealMethod();

		Mockito.when(bookReviewServ.getReview(Mockito.anyLong()))
		.thenThrow(new IllegalSelectorException());


		var books = bserv.getBooksCustomExceptionhandling().log();

		StepVerifier.create(books)
		.expectError(BookException.class)
		.verify(); //because when error occures we cannot complet the task so used only verify()

	}

	@Test  //Test when there is an Error   //this retry will continue not end 
	void getBooksMockOnRetry() {

		Mockito.when(bookInfoServ.getBooks())
		.thenCallRealMethod();

		Mockito.when(bookReviewServ.getReview(Mockito.anyLong()))
		.thenThrow(new IllegalSelectorException());


		var books = bserv.getBooksErrorRetry().log();

		StepVerifier.create(books)
		.expectError(BookException.class)
		.verify(); //because when error occures we cannot complet the task so used only verify()

	}

	@Test //getBooksErrorRetryN

	void getBooksErrorRetryN() {

		Mockito.when(bookInfoServ.getBooks())
		.thenCallRealMethod();

		Mockito.when(bookReviewServ.getReview(Mockito.anyLong()))
		.thenThrow(new IllegalSelectorException());


		var books = bserv.getBooksErrorRetryN().log();

		StepVerifier.create(books)
		.expectError(BookException.class)
		.verify(); //because when error occures we cannot complet the task so used only verify()

	}

	@Test
	void getBooksErrorRetryWhen() {
		Mockito.when(bookInfoServ.getBooks())
		.thenCallRealMethod();

		Mockito.when(bookReviewServ.getReview(Mockito.anyLong()))
		.thenThrow(new IllegalSelectorException());


		var books = bserv.getBooksErrorRetryWhen().log(); 
		StepVerifier.create(books)
		.expectError(BookException.class)
		.verify(); //because when error occures we cannot complet the task so used only verify()

	}
	
	@Test
	void getBooksErrorRetryWhenParticularServ() {
		Mockito.when(bookInfoServ.getBooks())
		.thenCallRealMethod();

		Mockito.when(bookReviewServ.getReview(Mockito.anyLong()))
		.thenThrow(new IllegalSelectorException());


		var books = bserv.getBooksErrorRetryWhenParticularServ().log(); 
		StepVerifier.create(books)
		.expectError(BookException.class)
		.verify();
		
	}

}
