package com.lec.wenance.challenge.api.repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lec.wenance.challenge.api.exception.EntityNotFoundException;
import com.lec.wenance.challenge.api.model.BitcoinPrice;

import reactor.core.publisher.Mono;

@Component
public class BitcoinRepository {
	private static Logger log = LoggerFactory.getLogger(BitcoinRepository.class);
	
	//Estructura donde alojamos los datos en memoria
	private LinkedList<BitcoinPrice> bitcoinPriceList = new LinkedList<BitcoinPrice>(); 
	
	/**
	 * Inserta las entidades de CEX desde el scheduler 
	 * @param bitcoinPrice
	 */
	public void insertBitcoinPrice(BitcoinPrice bitcoinPrice) {
		bitcoinPriceList.add(bitcoinPrice);
	}
	
	
	/**
	 * Buscamos el precio de Bitcoin en un tiempo determinado
	 * @param timestamp
	 * @return precio en timestamp
	 * @throws EntityNotFoundException
	 */
	public Mono<BitcoinPrice> findBitcoinPriceInTimestamp(long timestamp) throws EntityNotFoundException {
		
		BitcoinPrice bitcoinPriceResult = bitcoinPriceList.stream()
				.filter( price -> price.getTimestamp() <=  timestamp )
				.reduce((first , second ) -> second )
				.orElseThrow(() -> new EntityNotFoundException("Price not found for the given time : " + new Timestamp(timestamp)));
		
		return Mono.just(bitcoinPriceResult); 
	}
	
	
	/**
	 *  
	 * Buscamos el promedio de precios en un periodo determinado
	 * 
	 * @param timeFrom
	 * @param timeTo
	 * @return promedio
	 * @throws EntityNotFoundException
	 */
	public double findBitcoinAvergePriceByPeriod(long timeFrom, long timeTo) throws EntityNotFoundException {
		//Buscamos el precio inicial del periodo
		BitcoinPrice initPrice = findBitcoinPriceInTimestamp(timeFrom).block();
		//Bucamos todos los precios contenidos dentro del periodo
		List<BigDecimal> periodPriceList = bitcoinPriceList.stream()
				.filter( price -> initPrice.getTimestamp() <= price.getTimestamp() && price.getTimestamp() <= timeTo )
				.map(BitcoinPrice::getPrice)
				.collect(Collectors.toList());
		
		if(periodPriceList.isEmpty())
			throw new EntityNotFoundException("No se encontraron precios para el periodo ingresado");
		
		int cantidad = periodPriceList.size();
		//Sumamos todos los precios del periodo y calculamos el promedio
		BigDecimal sumatoria = periodPriceList.stream().reduce((a , b) -> a.add(b) ).orElse(BigDecimal.valueOf(0));	
		double average = sumatoria.doubleValue() / cantidad;
		
		return average; 
	}
	
	/**
	 * Buscamos la diferencia porcentual del precio promedio con el maximo precio
	 * @param precio promedio
	 * @return porcentaje
	 */
	public double findPercentageDifference( double average ) {
		//Obtenemos el maximo de todos los precios en memoria
		double max = bitcoinPriceList.stream()
				.mapToDouble( price -> price.getPrice().doubleValue() )
				.max().orElse(0);
		//Calculamos el porcentaje
		double percenatageDifference = 100 * ((max - average) / max);
		return percenatageDifference;
	}
	
}
