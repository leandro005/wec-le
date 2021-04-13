package com.lec.wenance.challenge.api.repository;

import java.sql.Timestamp;
import java.util.LinkedList;

import org.springframework.stereotype.Service;

import com.lec.wenance.challenge.api.model.BitcoinPrice;

import reactor.core.publisher.Mono;

@Service
public class BitcoinRepository {
	
	private LinkedList<BitcoinPrice> bitcoinPriceList = new LinkedList<BitcoinPrice>(); 
	
	public void insertBitcoinPrice(BitcoinPrice bitcoinPrice) {
		bitcoinPriceList.add(bitcoinPrice);
	}
	
	public Mono<BitcoinPrice> findBitcoinPriceInTimestamp(long timestamp) throws Exception {
		
		BitcoinPrice bitcoinPriceResult = bitcoinPriceList.stream()
				.filter( price -> price.getTimestamp() <=  timestamp )
				.reduce((first , second ) -> second )
				.orElseThrow(() -> new Exception("Price not found at time : " + timestamp));//Hacer EntityNotFoundException
		
		return Mono.just(bitcoinPriceResult); 
	}
	
	
}
