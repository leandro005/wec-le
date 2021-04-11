package com.lec.wenance.challenge.scheduler.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lec.wenance.challenge.scheduler.reponse.CEXLastPriceResponse;

import reactor.core.publisher.Mono;

@Service
public class CEXLastPriceService {
	
	String uri = "https://cex.io/api/last_price/BTC/USD";
		
	@Autowired
	private WebClient.Builder webClient;
	
	
	public CEXLastPriceResponse getCEXLastPriceResponse() {
		
		Mono<CEXLastPriceResponse> response = webClient
			.exchangeStrategies(ExchangeStrategies.builder().codecs((configurer) -> {
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				configurer.customCodecs().register(
            		new Jackson2JsonDecoder(mapper, MimeTypeUtils.parseMimeType("text/json")));
			}).build())
			.build()
			.get()
			.uri(uri)
			.retrieve()
			.bodyToMono(CEXLastPriceResponse.class);
		return response.block();
	}
	
	
}
