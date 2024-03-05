package com.mono.flux.controllers;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mono.flux.beans.CutomerEvent;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@RestController
public class MonoAndFlux {
	
	@GetMapping("/monoEvent")
	public Mono<CutomerEvent> monoObect(){
		CutomerEvent cutomerEvent=new CutomerEvent("Mono Data", new Date());
		return Mono.justOrEmpty(cutomerEvent);
	}
	
	@GetMapping(value="/fluxObjects", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Object> fluxObjects(){
		//CutomerEvent cutomerEvent=new CutomerEvent("FluxData", new Date());
		List<CutomerEvent> cutomerEventLiist = Arrays.asList(new CutomerEvent("one datat", new Date()),new CutomerEvent("two data", new Date()),new CutomerEvent("three data", new Date()));
		Stream<List<CutomerEvent>> stream = Stream.generate(()-> cutomerEventLiist);
		Flux<List<CutomerEvent>> dataflux = Flux.fromStream(stream);
		Flux<Long> intervalFlux = Flux.interval(Duration.ofSeconds(5));
		Flux<Tuple2<Long, List<CutomerEvent>>> zip = Flux.zip(intervalFlux, dataflux);
		Flux<Object> flux = zip.map(Tuple2::getT2);
		return flux;
	}

}
