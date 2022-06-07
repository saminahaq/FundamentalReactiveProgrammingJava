package demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import reactor.test.StepVerifier;

class BookServiceTest {

	private BookInfoService bookInfoServ = 
			new BookInfoService();
	
	private BookReviewService bookReviewServ =
			new BookReviewService(); 
	
	private BookService bookserv =
			new BookService(bookInfoServ,bookReviewServ);
	
	
	@Test
	void testGetBooks() {
		var books = bookserv.getBooks();
		
		StepVerifier.create(books)
				.assertNext(book -> {
					assertEquals("Book One",book.getBookInfo().getTitle());
					assertEquals(2,book.getBookreview().size());
				})
				.assertNext( book -> {
					assertEquals("Book Two",book.getBookInfo().getTitle());
					assertEquals(2,book.getBookreview().size());
				})
				.assertNext( book -> {
					assertEquals("Book Three",book.getBookInfo().getTitle());
					assertEquals(2,book.getBookreview().size());
				})
				.verifyComplete();
	}
	@Test
	void getBookById() {
		var book = bookserv.getBookById(1).log();
		
		StepVerifier.create(book) 
					.assertNext( b -> {
						assertEquals("Book One",b.getBookInfo().getTitle());
						assertEquals(2,b.getBookreview().size());
					})
					.verifyComplete();
		
		
	}

}
