import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

public class BackPressureTest {
	
	
	@Test
	public void testBackPressure() {
		var numbers = Flux.range(1, 100).log();
		//number.subscribe( integer -> System.out.println(integer)); //For simple range data

		//for large data : beg

			numbers.subscribe(new BaseSubscriber<Integer>() {
	            @Override
	            protected void hookOnSubscribe(Subscription subscription) {
	                request(3); //app handle only 3 record
	            }

	            @Override
	            protected void hookOnNext(Integer value) {
	                System.out.println("value = " + value);
	                if(value ==3) cancel(); // here we are canceling the data more than 3 record
	            }

	            @Override
	            protected void hookOnComplete() {
	                System.out.println("Completed!!");
	            }

	            @Override
	            protected void hookOnError(Throwable throwable) {
	                super.hookOnError(throwable);
	            }

	            @Override
	            protected void hookOnCancel() {
	                super.hookOnCancel();
	            }
	        });//for large data : beg
	}		
			
	
	//onBackpressureDrop Operator  -> tells what happend on the data that we droped after 3 record, also one time call publishers
			@Test
		    public void testBackPressureDrop() {
		        var numbers = Flux.range(1,100).log();
		        //numbers.subscribe(integer -> System.out.println("integer = " + integer));

		        numbers.onBackpressureDrop(integer -> {  //using onBackpressureDrop
		                    System.out.println("Dropped Values = " + integer);
		                })
		                .subscribe(new BaseSubscriber<Integer>() {
		            @Override
		            protected void hookOnSubscribe(Subscription subscription) {
		                request(3);
		            }

		            @Override
		            protected void hookOnNext(Integer value) {
		                System.out.println("value = " + value);
		                if(value ==3) hookOnCancel();
		            }

		            @Override
		            protected void hookOnComplete() {
		                System.out.println("Completed!!");
		            }

		            @Override
		            protected void hookOnError(Throwable throwable) {
		                super.hookOnError(throwable);
		            }

		            @Override
		            protected void hookOnCancel() {
		                super.hookOnCancel();
		            }
		        });
		    }

    //onbackPressureBuffer Operator	-> it will be buffereing the data that provide, so always keep the data at the buffer that we droped but not all data only few data	
			@Test
		    public void testBackPressureBuffer() {
		        var numbers = Flux.range(1,100).log();
		        //numbers.subscribe(integer -> System.out.println("integer = " + integer));

		        numbers
		                .onBackpressureBuffer(10,
		                        i -> System.out.println("Buffered Value = " + i))
		                .subscribe(new BaseSubscriber<Integer>() {
		                    @Override
		                    protected void hookOnSubscribe(Subscription subscription) {
		                        request(3);
		                    }

		                    @Override
		                    protected void hookOnNext(Integer value) {
		                        System.out.println("value = " + value);
		                        if(value ==3) hookOnCancel();
		                    }

		                    @Override
		                    protected void hookOnComplete() {
		                        System.out.println("Completed!!");
		                    }

		                    @Override
		                    protected void hookOnError(Throwable throwable) {
		                        super.hookOnError(throwable);
		                    }

		                    @Override
		                    protected void hookOnCancel() {
		                        super.hookOnCancel();
		                    }
		                });
		    }

    //onBackPressureError operator -> this particular error is handled and print
		    @Test
		    public void testBackPressureError() {
		        var numbers = Flux.range(1,100).log();
		        //numbers.subscribe(integer -> System.out.println("integer = " + integer));

		        numbers
		                .onBackpressureError()
		                .subscribe(new BaseSubscriber<Integer>() {
		                    @Override
		                    protected void hookOnSubscribe(Subscription subscription) {
		                        request(3);
		                    }

		                    @Override
		                    protected void hookOnNext(Integer value) {
		                        System.out.println("value = " + value);
		                        if(value ==3) hookOnCancel();
		                    }

		                    @Override
		                    protected void hookOnComplete() {
		                        System.out.println("Completed!!");
		                    }

		                    @Override
		                    protected void hookOnError(Throwable throwable) {
		                        System.out.println("throwable = " + throwable);
		                    }

		                    @Override
		                    protected void hookOnCancel() {
		                        super.hookOnCancel();
		                    }
		                });
		    }		
	    
	
}