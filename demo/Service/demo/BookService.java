package demo;

import java.time.Duration;
import java.util.List;

import org.springframework.boot.autoconfigure.amqp.RabbitProperties.Retry;

import Exception.BookException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry.RetrySignal;
import reactor.util.retry.RetryBackoffSpec;

@Slf4j
public class BookService {
	
	//e.g API # 01
	private BookInfoService bookInfoServ;
	//e.g API # 02
	private BookReviewService bookReviewServ;
	
	
	public BookService(BookInfoService bookInfoServ, BookReviewService bookReviewServ) {
		this.bookInfoServ = bookInfoServ;
		this.bookReviewServ = bookReviewServ;
	}
	
	//Getting allBooks
	 public Flux<Book> getBooks(){
		 var allBooks = bookInfoServ.getBooks();
		 return allBooks
				 .flatMap( bookInfo -> {
					 Mono<List<Review>> reviews =
							 bookReviewServ.getReview(bookInfo.getBookId()).collectList();
					 return reviews
							 .map(review -> new Book(bookInfo,review)
									); //combine both operator result "bookInfo" and "review"
				 }).onErrorMap(throwable -> {  //Custom Exception handling
					  System.out.print("throwable : "+ throwable);
					  return new BookException("ExceptionOccured while fetching Book "); //IllegalStateException("From Error Map");
				  })
				 .retry()  //retry() and retry(n)
				 .log();
	 }
	 
	 //Getting bookById
	 public Mono<Book> getBookById(long bookId){
		 var book = bookInfoServ.getBookById(bookId);
		 var review = bookReviewServ
				 .getReview(bookId)
				 .collectList();
		 
		 return book.zipWith(review,(b,r) -> new Book(b,r));
		 
	 }

	 //Custom Exception handling
	 /*
	  * Exception is inavaitable 
	  * so the exception from different API, need to Handle here
	  * so usually need to put "OnErrorMap()" exception because clinets may have several exception so need to customized the exceptio alltogether
	  * CustomException
	  * 
	  * 
	  * create BookException class and test with the  Mockito 
	  */
	 public Flux<Book> getBooksCustomExceptionhandling(){
		 var allBooks = bookInfoServ.getBooks();
		 return allBooks
				 .flatMap( bookInfo -> {
					 Mono<List<Review>> reviews =
							 bookReviewServ.getReview(bookInfo.getBookId()).collectList();
					 return reviews
							 .map(review -> new Book(bookInfo,review)); 
				 }).onErrorMap(throwable -> {  //Custom Exception handling
					  System.out.print("throwable : "+ throwable);
					  return new BookException("ExceptionOccured while fetching Book "); //IllegalStateException("From Error Map");
				  })
				 .log();
	 }


	 //retry() and retry(n)
	 /*
	  * when we are using server so sometimes server has occuer some exception so 
	  * we need to retry method in reactive to retry to connect with the server
	  * retry - try indefinite time or retry 
	  * retry(n) - telling them that many time
	  */
	 
	 //retry()
	 public Flux<Book> getBooksErrorRetry(){
		 var allBooks = bookInfoServ.getBooks();
		 return allBooks
				 .flatMap( bookInfo -> {
					 Mono<List<Review>> reviews =
							 bookReviewServ.getReview(bookInfo.getBookId()).collectList();
					 return reviews
							 .map(review -> new Book(bookInfo,review)); 
				 }).onErrorMap(throwable -> {  //Custom Exception handling
					  System.out.print("throwable : "+ throwable);
					  return new BookException("ExceptionOccured while fetching Book "); //IllegalStateException("From Error Map");
				  })
				 .retry()
				 .log();
	 }
	 
	//retry(n)
		 public Flux<Book> getBooksErrorRetryN(){
			 var allBooks = bookInfoServ.getBooks();
			 return allBooks
					 .flatMap( bookInfo -> {
						 Mono<List<Review>> reviews =
								 bookReviewServ.getReview(bookInfo.getBookId()).collectList();
						 return reviews
								 .map(review -> new Book(bookInfo,review)); 
					 }).onErrorMap(throwable -> {  //Custom Exception handling
						  System.out.print("throwable : "+ throwable);
						  return new BookException("ExceptionOccured while fetching Book "); //IllegalStateException("From Error Map");
					  })
					 .retry(3)
					 .log();
		 }
		 
	
	 //retryWhen()
	/*
	 * now when retry on particular scenario e.g databse call , API call so on that particular call retry	 
	 */
	
		 public Flux<Book> getBooksErrorRetryWhen(){
			 var allBooks = bookInfoServ.getBooks();
			 var retrySpacs = RetryBackoffSpec.backoff(
					 3,
					 Duration.ofMillis(2)
					 )//uptill now it throw the exception in the onerrorMap as well, so we need to bypass onErrorMap exception now
					 .onRetryExhaustedThrow((RetryBackoffSpec,RetrySignal) ->
					 	 Exceptions.propagate(RetrySignal.failure())
					 );
			 			
					 
			 return allBooks
					 .flatMap( bookInfo -> {
						 Mono<List<Review>> reviews =
								 bookReviewServ.getReview(bookInfo.getBookId()).collectList();
						 return reviews
								 .map(review -> new Book(bookInfo,review)); 
					 }).onErrorMap(throwable -> {  //Custom Exception handling
						  System.out.print("throwable : "+ throwable);
						  return new BookException("ExceptionOccured while fetching Book "); //IllegalStateException("From Error Map");
					  })
					 .retryWhen(retrySpacs)
					 .log();
		 }
		 
		//its retry on particular service "BookException"
	/*	 public Flux<Book> getBooksErrorRetryWhenParticularServ(){
			 var allBooks = bookInfoServ.getBooks();
			//only on the BookException this try methods will execute
			 var retrySpacs = RetryBackoffSpec.backoff(
					 3,
					 Duration.ofMillis(2)
					 ).filter(throwable -> throwable instanceof BookException)
					 .onRetryExhaustedThrow((RetryBackoffSpec,RetrySignal) ->
					 	 Exceptions.propagate(RetrySignal.failure())
					 );
			 			
					 
			 return allBooks
					 .flatMap( bookInfo -> {
						 Mono<List<Review>> reviews =
								 bookReviewServ.getReview(bookInfo.getBookId()).collectList();
						 return reviews
								 .map(review -> new Book(bookInfo,review)); 
					 }).onErrorMap(throwable -> {  //Custom Exception handling
						  System.out.print("throwable : "+ throwable);
						  return new BookException("ExceptionOccured while fetching Book "); //IllegalStateException("From Error Map");
					  })
					 .retryWhen(retrySpacs)
					 .log();
		 }*/
		 
		 /////////////////OR //////////////////////////// making the function method ->right click -> refactor -> extract method
		 
		 
		 public Flux<Book> getBooksErrorRetryWhenParticularServ(){
			 var allBooks = bookInfoServ.getBooks();
			//only on the BookException this try methods will execute
			// var retrySpacs = retryBackoffSpec();
			 					 
			 return allBooks 
					 .checkpoint("Error Checkpoint1")
					 .flatMap( bookInfo -> {
						 Mono<List<Review>> reviews =
								 bookReviewServ.getReview(bookInfo.getBookId()).collectList();
						 return reviews
								 .map(review -> new Book(bookInfo,review)); 
					 }).onErrorMap(throwable -> {  //Custom Exception handling
						  System.out.print("throwable : "+ throwable);
						  return new BookException("ExceptionOccured while fetching Book "); //IllegalStateException("From Error Map");
					  })
					 .retryWhen(retryBackoffSpec())
					 .log();
		 }

		private RetryBackoffSpec retryBackoffSpec() {
			Hooks.onOperatorDebug();
			return RetryBackoffSpec.backoff(
					 3,
					 Duration.ofMillis(2)
					 ).filter(throwable -> throwable instanceof BookException)
					 .onRetryExhaustedThrow((RetryBackoffSpec,RetrySignal) ->
					 	 Exceptions.propagate(RetrySignal.failure())
					 );
		} 
		 
		 
		 
		 
	   //Backpressure example
		 //onBackpressureDrop Operator
		 //onbackPressureBuffer Operator
		 //onBackPressureError operator
		 
		 /*
		  * how we control the backpresure here
		  * create a backpressuretest class 
		  */
		 

	 //Hot and Cold Streams
	/*
	 * cold stream is stream of data that will subscribe and get indefinit time every time, get same record at every time 
	 * e.g http calls database call
	 * 
	 * Hot Stream	
	 * receiving data at some interval time
	 * e.g stock ticker, tracking information these are hot stream 
	 * 
	 * 
	 * see HotAndColdStreamsTest.java
	 */
		
		
			
	 //Debugging Reactive Streams
	 
	 /*
	  * debuge operator
	  * 
	  * Method #01
	  * Hooks.onOperatorDebug();  //not the best way b/c its slow down
	  * so other is put check points , so every operaot put check points 
	  * 
	  *  Method #02
	  * .map().checkpoint("ErrorcheckPoint1");
	  * put at multi-places 
	  * 
	  *  Method #03
	  *  
	  *  add dependency
	  *  
	  *  reactor tools
	  *  step # 01
	  *   added at pom
	  *  <dependency>
			<groupId>io.projectreactor</groupId>
			<artifactId>reactor-tools</artifactId>
		</dependency>
	  * 
	  * once its added 
	  * 
	  * 
	  * step # 02
	  * ReactorDebugAgent.init();
	  * 
	  * at main file inside
	  * 
			public static void main(String[] args) {
			ReactorDebugAgent.init();
			SpringApplication.run(ReactiveProgrammingTutorialApplication.class, args);
	     }
	  * 
	  * 
	  * if using in class 
	  * ReactorDebugAgent.init();
	  * ReactorDebugAgent.processExsistingClasses()
	  * 
	  */
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 

}
