package service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import reactor.test.StepVerifier;

class FluxandMonoServicesTest {
//here we are just doing basic java test using project reator
	
	FluxandMonoServices serv = 
			new FluxandMonoServices();
	
	
	@Test
	void testFruitFlux() {
		var fruitFlux =serv.fruitFlux().log();
		
		StepVerifier.create(fruitFlux)
					.expectNext("Mango", "Dragon Fruit","Kiwi")
					.verifyComplete();
	}

	@Test
	void testFruitMono() {
		var fruitMono = serv.fruitMono().log();
		
		StepVerifier.create(fruitMono)
					.expectNext("Mango")
					.verifyComplete();
		
	}
	@Test
	void flowerFluxMap() {
		var flowerFlux = serv.flowerFluxMap().log();
		
		StepVerifier.create(flowerFlux)
					.expectNext("JASMINE","LILLY")
					.verifyComplete(); 
	}
	
	@Test
	void flowerLengthOperator() {
		var flowerlength = serv.flowerLengthOperator(5).log();
		
		StepVerifier.create(flowerlength)
					.expectNext("Jasmine")
					.verifyComplete();
	}

	@Test
	void flowerMapFilter() {
		var flowerMapFilter = serv.flowerMapFilter(5).log();
		
		StepVerifier.create(flowerMapFilter)
					.expectNext("JASMINE")
					.verifyComplete();
							
	}
	@Test
	void flowerFLatMap() {
		var flowerFlatMap = serv.flowerFLatMap().log();
		
		StepVerifier.create(flowerFlatMap)
					.expectNextCount(12)
					.verifyComplete();
					
	}
	void flowerFLatMapAsync() {
		var flowerFlatMapAsync = serv.flowerFLatMapAsync().log();
		
		StepVerifier.create(flowerFlatMapAsync)
					.expectNextCount(12)
					.verifyComplete();
					
	}
	
	@Test
	void fruitFlatMapMono() {
		var fruitMonoFlatMap = serv.fruitFlatMapMono().log();
		
		StepVerifier.create(fruitMonoFlatMap)
					.expectNextCount(1)
					.verifyComplete();
				
		
	}
	@Test
	void flowerConcatMap() {
		var flowerConcatMap = serv.flowerConcatMap().log();
		
		StepVerifier.create(flowerConcatMap)
		.expectNextCount(12)
		.verifyComplete();
	}
	
	@Test
	void fruitFlatMapMany() {
		var flowerFlatMapMany = serv.fruitFlatMapMany().log();
		
		StepVerifier.create(flowerFlatMapMany)
					.expectNextCount(5)
					.verifyComplete();
	}
	
	@Test
	void flowerfluxTransform() {
		var flowerTransform = serv.flowerfluxTransform(5).log();
		
		StepVerifier.create(flowerTransform)
					.expectNext("Jasmine")
					.verifyComplete();
		
		
	}
	
	@Test
	void flowerfluxTransformDefaultIfEmpty() {
		var flowerTransformDefaultIfEmpty = serv.flowerfluxTransformDefaultIfEmpty(10).log();
		
		StepVerifier.create(flowerTransformDefaultIfEmpty)
		.expectNext("Default")
		.verifyComplete();
	}
	
	@Test
	void flowerfluxTransformswitchIfEmpty() {
		var flowerfluxTransformswitchIfEmpty = serv.flowerfluxTransformswitchIfEmpty(10).log();
		
		StepVerifier.create(flowerfluxTransformswitchIfEmpty)
		.expectNext("WhiteRedWhite Rose") //we can put more than one values if qualified
		.verifyComplete();
		
	}
	
	@Test
	void flowerConcat() {
		var flowerConcat = serv.flowerConcat();
		
		StepVerifier.create(flowerConcat)
					.expectNext("Rose","white Rose","white Lilly","Red Lilly")
					.verifyComplete();
					
	}
	
	@Test
	void flowerConcatWith() {
		var flowerConcat = serv.flowerConcat();
		
		StepVerifier.create(flowerConcat)
					.expectNext("Rose","white Rose","white Lilly","Red Lilly")
					.verifyComplete();
					
	}
	
	@Test
	void flowerMonoConcatWith() {
		var flowerMonoConcatWith = serv.flowerMonoConcatWith().log();
		
		StepVerifier.create(flowerMonoConcatWith)
		.expectNext("Rose","white Lilly")
		.verifyComplete();
		
	}
	
	@Test
	void FruitFluxMerge() {
		var FruitFluxMerge = serv.FruitFluxMerge().log();
		
		StepVerifier.create(FruitFluxMerge)
		.expectNext("apple","banana","tomatto","cucumber")
		.verifyComplete();
		
	}
	
	@Test
	void FruitFluxMergeWith() {
		var FruitFluxMergeWith = serv.FruitFluxMergeWith().log();
		
		StepVerifier.create(FruitFluxMergeWith)
		.expectNext("apple","tomatto","banana","cucumber")
		.verifyComplete();
		
	}
	
	
	@Test
	void FruitFluxMergeWithSequential(){
		var FruitFluxMergeWithSequential = serv.FruitFluxMergeWithSequential().log();
		
		StepVerifier.create(FruitFluxMergeWithSequential)
					.expectNext("apple","banana","tomatto","cucumber")
					.verifyComplete();
		 }
	@Test
	void fruitFluxZip() {
		var fruitFluxZip = serv.fruitFluxZip().log();
		
		StepVerifier.create(fruitFluxZip)
		.expectNext("appletomatto","bananacucumber")
		.verifyComplete();
		}
	
	@Test
	void fruitFluxWithZip() {
		var fruitFluxWithZip = serv.fruitFluxWithZip().log();
		
		StepVerifier.create(fruitFluxWithZip)
		.expectNext("appletomatto","bananacucumber")
		.verifyComplete();
		}
	
	@Test
	void foodFluxZipTuple() {
		var foodFluxZipTuple= serv.foodFluxZipTuple().log();
		
		StepVerifier.create(foodFluxZipTuple)
		.expectNext("appletomattoRed bean","bananacucumberblack bean")
		.verifyComplete();
		}
	@Test
	void fruitMonoZipWith() {
		var fruitMonoZipWith= serv.fruitMonoZipWith().log();
		
		StepVerifier.create(fruitMonoZipWith)
		.expectNext("appletomatto")
		.verifyComplete();
	}
	@Test
		void fruitFluxDoOn() {
			var fruitFluxDoOn= serv.fruitFluxDoOn(5).log();
			
			StepVerifier.create(fruitFluxDoOn)
			.expectNext("Jasmine")
			.verifyComplete();
		
	}
	@Test
	void fruitsFluxOnErrorReturn() {
		var fruitsFluxOnErrorReturn= serv.fruitsFluxOnErrorReturn().log();
		
		StepVerifier.create(fruitsFluxOnErrorReturn)
		.expectNext("Orange","Mango","Banana")
		.verifyComplete();
	}
	@Test
	void fruitsFluxOnErrorContinue() {
		var fruitsFluxOnErrorContinue= serv.fruitsFluxOnErrorContinue().log();
		
		StepVerifier.create(fruitsFluxOnErrorContinue)
		.expectNext("ROSE","WHITEREDWHITE ROSE")
		.verifyComplete();
	}
	@Test
	void fruitsFluxonErrorMap() {
		var fruitsFluxonErrorMap= serv.fruitsFluxonErrorMap().log();
		
		StepVerifier.create(fruitsFluxonErrorMap)
		.expectNext("ROSE")
		.expectError(IllegalStateException.class)
		.verify(); //cannot go to verify complete because of illegal exception
	}
	@Test
	void fruitsFluxdoOnerror() {
		var fruitsFluxdoOnerror= serv.fruitsFluxdoOnerror().log();
		
		StepVerifier.create(fruitsFluxdoOnerror)
		.expectNext("ROSE")
		.expectError(RuntimeException.class)
		.verify(); //cannot go to verify complete because of illegal exception
	}
}
