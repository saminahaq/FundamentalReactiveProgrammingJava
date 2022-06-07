package service;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxandMonoServices {
	
	//Creating Flux
	public Flux<String> fruitFlux(){
		
		//this covert the list into the flux
		return Flux.fromIterable(List.of("Mango", "Dragon Fruit","Kiwi"));
		//return Flux.fromIterable(List.of("Mango", "Dragon Fruit","Kiwi")).log();
	}
	
	//*************************  Mono **********************
	
		//Creating Mono
		
			public Mono<String> fruitMono(){
				return Mono.just("Mango");
				//return Mono.just("Mango").log();
			}
		
			//*************************  Mono flatMap Operator **********************
			//because mono has 0 to 1 only so forst we rapped the string into one and than passed into the list
			
			public Mono<List <String>> fruitFlatMapMono(){
				return Mono.just("Mango")
						.flatMap(s -> Mono.just(List.of(s.split(""))));
			}
		
	
	
	//*************************  Map Operator **********************
    
	public Flux<String> flowerFluxMap(){
		return Flux.fromIterable(List.of("Jasmine","Lilly"))
				.map(String::toUpperCase);
		//.log();		
	}
			
	
	//*************************  Filter  Operator **********************
	
	public Flux<String> flowerLengthOperator(int number){
		return Flux.fromIterable(List.of("Jasmine","Lilly"))
					.filter(s -> s.length()  > number);
					
	}
	
	//*************************  Filter and Map Operator combined **********************
	
	
	public Flux<String> flowerMapFilter(int number){
		return Flux.fromIterable(List.of("Jasmine","Lilly"))
					.filter( s -> s.length() > number)
					.map(String::toUpperCase);
	}
	

	//*************************  flatMap Operator **********************
	//each and every elemented emmited convert that elements to Flux of 0 to n element
	//e.g Mango converteed into the Flux of 0 to N, so similary every element of list go through the flux
	//Synchronous
	public Flux<String> flowerFLatMap(){
		return Flux.fromIterable(List.of("Jasmine","Lilly"))
					.flatMap( s -> Flux.just(s.split("")));
						//	.log();
				
	}
	
	//Asynchronous
	public Flux<String> flowerFLatMapAsync(){
		return Flux.fromIterable(List.of("Jasmine","Lilly"))
					.flatMap( s -> Flux.just(s.split(""))
					.delayElements(Duration.ofMillis(
						 new Random().nextInt(1))));
			
	}
	//*************************  concatMap Operator  **********************
	//concat map preserved ordering as compare to the FlatMap, where FlatMap does't preserve the ordering
	
	public Flux<String> flowerConcatMap(){
		return Flux.fromIterable(List.of("Jasmine","Lilly"))
					.concatMap(s -> Flux.just(s.split(""))
					.delayElements(Duration.ofMillis(
							new Random().nextInt(1))));
	}
	
	//*************************  flatMapMany  Operator  **********************
	//convert Mono to flux
	 
	
	public Flux<String> fruitFlatMapMany(){
		return Mono.just("Mango")
				.flatMapMany(s -> Flux.just(s.split(""))).log();
	}

	//*************************  transform  Operator  **********************
	//take functional interface paramenter where it defined input type and output type
/* so here we are assigning the name of the customized filter function	
 * 
 * giving variable name for this "filter( s -> s.length() > number)" 
 * so we can use name for furthur in code insted of doing the filter 
 * through using the functional interface
 * 
 * by using this example " flowerMapFilter"
 * 
 * 
 */

	public Flux<String> flowerfluxTransform(int number){
		
		
		// Function<input data, output data> var_FilterName = data -> data.filter( put filter condition here); e.g condition =s -> s.length() > number
		
		Function<Flux<String> , Flux<String>> filterData
				= data -> data.filter(s -> s.length() > number);
				
		
		return Flux.fromIterable(List.of("Jasmine","Lilly"))
					.transform(filterData).log();
					
	}
		
		
	//************************* defaultIfEmpty Operator    **********************	
	/* where no data to be shared 
	e.g in test case if we put 10
	var flowerTransform = serv.flowerfluxTransform(10).log();
	and actually there is only 5 data so this type od scenario handled here
	Solution : send default id data */
	
  public Flux<String> flowerfluxTransformDefaultIfEmpty(int number){

		Function<Flux<String> , Flux<String>> filterData
				= data -> data.filter(s -> s.length() > number);
	
		return Flux.fromIterable(List.of("Jasmine","Lilly"))
					.transform(filterData)
					.defaultIfEmpty("Default")
					.log();
					
	}
	
	
//************************* switchIfEmpty Operator    **********************	
	/*If we are not emitting any data on the operator so we can used the different set of data
	 * 	for new data set, if initial failed
	 */
		
  	public Flux<String> flowerfluxTransformswitchIfEmpty(int number){

		Function<Flux<String> , Flux<String>> filterData
				= data -> data.filter(s -> s.length() > number);
	
		return Flux.fromIterable(List.of("Jasmine","Lilly"))
					.transform(filterData)
					.switchIfEmpty(Flux.just("Rose", "Red Rose", "WhiteRedWhite Rose")
						.transform(filterData))
					.log();
					
	}	
		
  //************************* concat & concatWith Operator   **********************	
	/* how to combine different reactive stream available e.g two mono combine and two flux availabel 
	 * thre is two method one concat and concatwith
	 * concat :	static method available 
	 * concatWith : Instant method available
	 * 
	 * combine two type , with the sequential order
	 * 
	 * scenario : getting the data from two database and then need to concat squentially 
	 */
  	
		//Concat
  	
  	public Flux<String> flowerConcat(){
  		Flux<String> flower1 = Flux.just("Rose", "white Rose");
  		Flux<String> flower2 = Flux.just("white Lilly", "Red Lilly");
  		
  		return Flux.concat(flower1, flower2).log();
  	}
		
  	//ConcatWith
  	/*
  	 * concatWith : look more clean and good
  	 */
  	
  	public Flux<String> flowerConcatWith(){
  		Flux<String> flower1 = Flux.just("Rose", "white Rose");
  		Flux<String> flower2 = Flux.just("white Lilly", "Red Lilly");
  		
  		return flower1.concatWith(flower2);
  	}
  	
  	//For mono : combining two mon and result in Flux
  	public Flux<String> flowerMonoConcatWith(){
  		Mono<String> flower1 = Mono.just("Rose");
  		Mono<String> flower2 = Mono.just("white Lilly");
  		
  		return flower1.concatWith(flower2);
  	}
  	
  	
  //************************* merge and mergeWith Operator   **********************	
  	/* merg and mergwith it will emit the data with the publisher fashion and getting data Asynchrounius
  	 * 
  	 */
  	//Merge
  	public Flux<String> FruitFluxMerge(){
  		
  		Flux<String> fruit = Flux.just("apple", "banana");
  		Flux<String> veggi = Flux.just("tomatto","cucumber");
  		
  		return Flux.merge(fruit,veggi).log();
  	}
  	
  	//Mergewith
  	
   public Flux<String> FruitFluxMergeWith(){
  		
  		Flux<String> fruit = Flux.just("apple", "banana")
  				.delayElements(Duration.ofMillis(1));
  		Flux<String> veggi = Flux.just("tomatto","cucumber")
  		        .delayElements(Duration.ofMillis(2));
  		return fruit.mergeWith(veggi);
  	}
  	
   //MergeWithSequential
   public Flux<String> FruitFluxMergeWithSequential(){
 		
	   Flux<String> fruit = Flux.just("apple", "banana")
 				.delayElements(Duration.ofMillis(1));
 		Flux<String> veggi = Flux.just("tomatto","cucumber")
 		        .delayElements(Duration.ofMillis(2));
 		
 		return Flux.mergeSequential(fruit,veggi).log();
 	}
 	
 //************************* zip and zipWith Operator  **********************	 	
  	/* so far we learn the operator that work on similar type of reative type
  	 * e.g Mono to Mono and Flux to Flux etc 
  	 * but when we have other operator to operate so we used Zip operator
  	 * upto the 8 pubslishers we can use in input
  	 */
  	
   //Zip
   public Flux<String> fruitFluxZip(){
	   
	   Flux<String> fruit = Flux.just("apple", "banana");
	   Flux<String> veggi = Flux.just("tomatto","cucumber");
	   
	   //here in return we can code the upto the 8 parameter
	   //it zip appletomatto and bananacucumber 
	   return Flux.zip(fruit, veggi,(first,second) -> first + second).log();
	   
   }
   
   //WithZip
   
   public Flux<String> fruitFluxWithZip(){
	   
	   Flux<String> fruit = Flux.just("apple", "banana");
	   Flux<String> veggi = Flux.just("tomatto","cucumber");
	   
	   return fruit.zipWith(veggi,(first,second) -> first + second).log();
	   
   }
   
   //Return ZipTuple
   
   public Flux<String> foodFluxZipTuple(){
	   
	   Flux<String> fruit = Flux.just("apple", "banana");
	   Flux<String> veggi = Flux.just("tomatto","cucumber");
	   Flux<String> foodbeans = Flux.just("Red bean","black bean");
	   
	   return Flux.zip(fruit, veggi,foodbeans)
			   		.map(objects -> objects.getT1() + objects.getT2() + objects.getT3()).log(); //here we are telling map operator about operator, go upto 8 publisher
   }
   
   //Mono ZipWith
   
 public Mono<String> fruitMonoZipWith(){
	   
	   Mono<String> fruit = Mono.just("apple");
	   Mono<String> veggi = Mono.just("tomatto");
	
	   return fruit.zipWith(veggi,(first,second) -> first + second).log();
	 }
   
   //************************* doOn* Callbacks **********************	 	  
   /*
    * these operator just the side effect of the operator, it would not change the operator effect these operator is debugging the operator pipline
    * main functionality would not change just doing extra behavior as debugger or side Effect
    * side effect/process or extra process, on the next operator doOnNext()
    * For debugging and notification purpose
    */

  public Flux<String> fruitFluxDoOn(int number){
	  
	  
	  return Flux.fromIterable(List.of("Jasmine","Lilly"))
				.filter(s -> s.length()  > number)
				.doOnNext( s-> {
					System.out.println("s = " + s);
				})
				.doOnSubscribe( subscription -> {
					System.out.println("On Subscription :" + subscription.toString());
				})
				.doOnComplete(() -> System.out.println("Completed !!!"));
  }
   
//************************* Exception Handling **********************	
   /*
    * whenever exception in reative stream that particuler stream stop emitting data from that point and passed the data untill than
    * there is two ways to handle Error
    * 1: TO handle particular Error
    * 2: Resume whenever that particular error 
    * onErrorReturn Operator
    * onErrorContinue Operator
    * onErrorMap Operator
    * doOnerror Operator
    * 
    */
   
  //onErrorReturn Operator -> send the default value when exception occured:
  
  public Flux<String> fruitsFluxOnErrorReturn(){
	  
	  return Flux.just("Orange","Mango")
			  .concatWith(Flux.error(
					  new RuntimeException("Exception Occured")
					  ))
			  .onErrorReturn("Banana");
  }
  
  //onErrorContinue Operator 
  
   /*onErrorContinue Operator -> so on that e.g ["Rose", "Red Rose", "WhiteRedWhite Rose"]
    * so if the Red Rose has error so it drop the Red rose and continue to the next "WhiteRedWhite Rose"
    */
   
   public Flux<String> fruitsFluxOnErrorContinue(){
	  
	  return Flux.just("Rose", "Red Rose", "WhiteRedWhite Rose")
			  .map(s -> {
				  if(s.equalsIgnoreCase("Red Rose"))
					  throw new RuntimeException("Runtime Exception");
				  return s.toUpperCase();
			  }).onErrorContinue((e,f) -> {
				 System.out.println("e = " + e); 
				 System.out.println("f = " + f);
			  }).log();
	 } 
   ///onErrorMap Operator -> Handle exception but not continue further
   
   /*
    * onErrorMap Operator -> used to map exception to other exception that application already haved e.g custom exception
    */
   public Flux<String> fruitsFluxonErrorMap(){
		  
		  return Flux.just("Rose", "Red Rose", "WhiteRedWhite Rose")
				  .map(s -> {
					  if(s.equalsIgnoreCase("Red Rose"))
						  throw new RuntimeException("Runtime Exception");
					  return s.toUpperCase();
					  //error on "Red Rose"  catch by the runtime exception, throwable catch the runtime exception and throw the illegal exception
				  }).onErrorMap(throwable -> {
					  System.out.print("throwable : "+ throwable);
					  return new IllegalStateException("From Error Map");
				  }).log(); //so only get the ROSE and illegal exception ".expectError(IllegalStateException.class)
		 } 
   
   //doOnerror Operator
   
   /*
    * doOnerror Operator -> similar to the try catch exception 
    * .doOnError() don't need to return the exception  like .onErrorMap
    */
   
   public Flux<String> fruitsFluxdoOnerror(){
		  
		  return Flux.just("Rose", "Red Rose", "WhiteRedWhite Rose")
				  .map(s -> {
					  if(s.equalsIgnoreCase("Red Rose"))
						  throw new RuntimeException("Runtime Exception");
					  return s.toUpperCase();
					  //error on "Red Rose"  catch by the runtime exception, throwable catch the runtime exception and throw the illegal exception
				  }).doOnError(throwable -> {
					  System.out.print("throwable : "+ throwable);
					 
				  }).log(); //so only get the ROSE and illegal exception ".expectError(IllegalStateException.class)
		 } 
   
   
   
   
   
   
   
   
	public static void main(String... args) {
		
		
		//*************************For Flux **********************
		FluxandMonoServices serv = 
				new FluxandMonoServices();
		//to run any of the Flux we need to "subscribe the FLux, then only we able to get the data, 
		//so only that publisher can emitted the data.
		//within teh subscriber put the lambda events
		// so here printing data
/*			serv.fruitFlux()
			.subscribe(s -> {
				System.out.println( "S =" + s);
			});
			
			
			
			//*************************For Mono **********************
			serv.fruitMono()
				.subscribe(s -> {
						System.out.println("s -> from Mono = " + s);
				});
            //************************* Mono flatMap Operator **********************	
			serv.fruitFlatMapMono()
				.subscribe( s-> {
					System.out.println(" Mono FlatMap : "+ s);
				});
			
		//*************************For Logging the Reactive Stream **********************
		//e.g onsubscribe() on necxt()....onComplete()  ; we can logged what happening on stream of data
		//return Flux.fromIterable(List.of("Mango", "Dragon Fruit","Kiwi")).log();
		//return Mono.just("Mango").log();
		
		
		//*************************  Junit Test for Flux and Mono **********************
		//Please see the Test folder
		
		//*************************  Map Operator Implementation **********************
		//Please see the Test folder for Operaton Testing
		
		serv.flowerFluxMap()
			.subscribe(s -> { 
				System.out.println("Operator = " + s);
			});
		
		
		//*************************  Filter  Operator Implementation**********************
		
		serv.flowerLengthOperator(5)
			.subscribe(s -> {
				System.out.println("Filter  :" + s);
			});
		
		//*************************  Filter and Map Operator combined **********************
		serv.flowerMapFilter(5)
			.subscribe(s -> {
				System.out.println("Filter and Map:  " + s);
			});
		
		//*************************  flatMap Operator **********************
		//sync : sequences fashion
		serv.flowerFLatMap()
			.subscribe(s -> {
				System.out.println("Flat Map :" + s);
			});
		
		//Async
		//here we wouldnot get the data in squences order
		serv.flowerFLatMapAsync()
			.subscribe(s -> {
				System.out.println("async Flat Map : " + s);
			});
		
		//*************************  concatMap Operator  **********************
	//sequential order emmited only difference bewtween this and FlatMap
		serv.flowerConcatMap()
			.subscribe(s -> {
				System.out.println(" Concat operator :" +s);
			});
	
		//*************************  flatMapMany  Operator  **********************
		//convert Mono to flux
		serv.fruitFlatMapMany()
			.subscribe(s ->{
				System.out.println(" Flat Map Many :" + s);
			});
	
		
		serv.flowerfluxTransform(5)
			.subscribe( s-> {
				System.out.println(" Transform Operator : "+ s);
			});
	

		
	serv.flowerfluxTransformDefaultIfEmpty(10)
		.subscribe(s -> {
			System.out.println("DefaultIfEmpty operator  :" + s);
		});
		
	
		serv.flowerfluxTransformswitchIfEmpty(10)
			.subscribe(s -> {
				System.out.print("switchIfEmpty  : " + s);
				
			});
		
		serv.flowerConcat()
			.subscribe( s -> {
				System.out.print("Concat : " + s);
			}); 
		
		serv.flowerConcatWith()
		.subscribe( s -> {
			System.out.print("Concat : " + s);
		});
		
		
		serv.flowerMonoConcatWith()
			.subscribe( s -> {
				System.out.println("MonoConcatWith  " +s );
			}); 
		
		serv.FruitFluxMerge()	
			.subscribe(s -> {
				System.out.println("Merge  : " + s);
			});
		
		serv.FruitFluxMergeWith ()	
		.subscribe(s -> {
			System.out.println("Merge  : " + s);
		});
		
		serv.FruitFluxMergeWithSequential()	
		.subscribe(s -> {
			System.out.println("Merge with Sequental : " + s);
		});
		
		serv.fruitFluxZip()
			.subscribe( s -> {
				System.out.println("Zip Operator : "+ s);
			}); 
		
		serv.fruitFluxWithZip()
		.subscribe( s -> {
			System.out.println("ZipWith Operator : "+ s);
		});
	
		serv.foodFluxZipTuple()
		.subscribe( s -> {
			System.out.println("ZipWith Operator : "+ s);
		});
		
		serv.fruitMonoZipWith()
		.subscribe( s -> {
			System.out.println("Mono  ZipWith Operator : "+ s);
		});
		 
		serv.fruitFluxDoOn(5)
		.subscribe( s -> {
			System.out.println("DoOn Operator : "+ s);
		});
		
		serv.fruitsFluxonErrorMap()
		.subscribe( s -> {
			System.out.println("OnErrorContinue Operator : "+ s);
		});
		serv.fruitsFluxdoOnerror()
		.subscribe( s -> {
			System.out.println("doOnerror Operator : "+ s);
		}); 
		*/
		serv.fruitsFluxdoOnerror()
		.subscribe( s -> {
			System.out.println("OnErrorContinue Operator : "+ s);
		}); 
	}
	
}
