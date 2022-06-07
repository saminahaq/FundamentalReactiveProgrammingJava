package demo;

import java.util.List;

import reactor.core.publisher.Flux;

public class BookReviewService {
//fetching review for books
	
	public Flux<Review> getReview(long bookId){
		
		var reviews =  List.of(
				new Review(1,bookId,9.1,"Good Book"),
                new Review(2,bookId,8.6,"Worth Reading")
			);
		return Flux.fromIterable(reviews); 
	}
	
	
}
