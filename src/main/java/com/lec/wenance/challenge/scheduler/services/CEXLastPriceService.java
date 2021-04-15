package com.lec.wenance.challenge.scheduler.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lec.wenance.challenge.scheduler.dto.CEXLastPrice;

import reactor.core.publisher.Mono;

@Service
public class CEXLastPriceService {
	
	String uri = "https://cex.io/api/last_price/BTC/USD";
		
	@Autowired
	private WebClient.Builder webClient;
	
	
	public CEXLastPrice getCEXLastPriceResponse() {
		
		Mono<CEXLastPrice> response = webClient
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
			.bodyToMono(CEXLastPrice.class);
		return response.block();
	}
	
	
}
