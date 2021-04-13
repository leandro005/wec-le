package com.lec.wenance.challenge.api.repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.lec.wenance.challenge.api.exception.EntityNotFoundException;
import com.lec.wenance.challenge.api.model.BitcoinPrice;

import reactor.core.publisher.Mono;

@Service
public class BitcoinRepository {
	private static Logger log = LoggerFactory.getLogger(BitcoinRepository.class);
	
	private LinkedList<BitcoinPrice> bitcoinPriceList = new LinkedList<BitcoinPrice>(); 
	
	public void insertBitcoinPrice(BitcoinPrice bitcoinPrice) {
		bitcoinPriceList.add(bitcoinPrice);
	}
	
	public Mono<BitcoinPrice> findBitcoinPriceInTimestamp(long timestamp) throws EntityNotFoundException {
		
		BitcoinPrice bitcoinPriceResult = bitcoinPriceList.stream()
				.filter( price -> price.getTimestamp() <=  timestamp )
				.reduce((first , second ) -> second )
				.orElseThrow(() -> new EntityNotFoundException("Price not found for the given time : " + new Timestamp(timestamp)));
		
		return Mono.just(bitcoinPriceResult); 
	}
	
	
	public double findBitcoinAvergePriceByPeriod(long timeFrom, long timeTo) throws EntityNotFoundException {
		
		BitcoinPrice initPrice = findBitcoinPriceInTimestamp(timeFrom).block();
		
		List<BigDecimal> periodPriceList = bitcoinPriceList.stream()
				.filter( price -> initPrice.getTimestamp() <= price.getTimestamp() && price.getTimestamp() <= timeTo )
				.map(BitcoinPrice::getPrice)
				.collect(Collectors.toList());
		
		if(periodPriceList.isEmpty())
			throw new EntityNotFoundException("No se encontraron precios para el periodo ingresado");
		
		int cantidad = periodPriceList.size();
		
		BigDecimal sumatoria = periodPriceList.stream().reduce((a , b) -> a.add(b) ).orElse(BigDecimal.valueOf(0));	
		
		double average = sumatoria.doubleValue() / cantidad;
		
		log.info(new Timestamp(timeTo).toString());
		
		return average; 
	}
	
	public double findPercentageDifference( double average ) {
		double max = bitcoinPriceList.stream()
				.mapToDouble( price -> price.getPrice().doubleValue() )
				.max().orElse(0);
		
		double percenatageDifference = 100 * ((max - average) / max);
		
		return percenatageDifference;
	}
	
}
