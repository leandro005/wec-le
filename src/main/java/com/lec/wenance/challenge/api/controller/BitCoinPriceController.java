package com.lec.wenance.challenge.api.controller;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lec.wenance.challenge.api.exception.EntityNotFoundException;
import com.lec.wenance.challenge.api.model.BitcoinAveragePercentageResponse;
import com.lec.wenance.challenge.api.model.BitcoinPrice;
import com.lec.wenance.challenge.api.model.BitcoinPriceResponse;
import com.lec.wenance.challenge.api.repository.BitcoinRepository;

@RestController
@RequestMapping("/wcl")
public class BitCoinPriceController {
	private static Logger log = LoggerFactory.getLogger(BitCoinPriceController.class);
	
	@Autowired
	private BitcoinRepository bitcoinRepository;
	
	@RequestMapping(value= "/priceByTimestamp/{time}", method = RequestMethod.GET, produces = "application/json" )
	public @ResponseBody ResponseEntity<BitcoinPriceResponse> priceByTimestamp(@PathVariable String time){
		BitcoinPriceResponse restResponse = new BitcoinPriceResponse();
		BitcoinPrice bitcoinPrice = null;
		try {
			long timeLong = Timestamp.valueOf(time).getTime(); 
			bitcoinPrice = bitcoinRepository.findBitcoinPriceInTimestamp(timeLong).block();
			restResponse.setPrecio(bitcoinPrice.getPrice().toString());
			restResponse.setMessage("OK con la busqueda");
		} catch (EntityNotFoundException e) {
			log.error("Error al intentar recuperar el precio: ", e );
			restResponse.setPrecio("");
			restResponse.setMessage(e.getMessage());
			return new ResponseEntity<BitcoinPriceResponse>(restResponse, HttpStatus.NOT_FOUND);
		} catch (IllegalArgumentException e) {
			log.error("Error al intentar recuperar el precio: ", e );
			restResponse.setPrecio("");
			restResponse.setMessage("Los datos de entrada son incorrectos");
			return new ResponseEntity<BitcoinPriceResponse>(restResponse, HttpStatus.BAD_REQUEST);	
		} catch (Exception e) {
			log.error("Error al intentar recuperar el precio: ", e );
			restResponse.setPrecio("");
			restResponse.setMessage(e.getMessage());
			return new ResponseEntity<BitcoinPriceResponse>(restResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
		return new ResponseEntity<BitcoinPriceResponse>(restResponse, HttpStatus.OK);
	}
	
	@RequestMapping(value= "/averageAndDiffPrice/{timeFrom}/{timeTo}", method = RequestMethod.GET, produces = "application/json" )
	public @ResponseBody ResponseEntity<BitcoinAveragePercentageResponse> averageAndPercentDifference( @PathVariable String timeFrom, 
			 @PathVariable String timeTo){
		BitcoinAveragePercentageResponse averageAndPercentDiffResponse = new BitcoinAveragePercentageResponse();
		try {
			//Formateamos las fechas
			long timeFromLong = Timestamp.valueOf(timeFrom).getTime(); 
			long timeToLong = Timestamp.valueOf(timeTo).getTime();
			//Lanzamos llamada al metodo que recupera el promedio
			double average = bitcoinRepository.findBitcoinAvergePriceByPeriod(Long.valueOf(timeFromLong).longValue(), 
					Long.valueOf(timeToLong).longValue());
			//Lanzamos llamada al metodo que recupera la diferencia porcentual			
			double percentage = bitcoinRepository.findPercentageDifference(average);
			averageAndPercentDiffResponse.setAverage(String.valueOf(average));
			averageAndPercentDiffResponse.setPercentage(String.valueOf(percentage));
			averageAndPercentDiffResponse.setMessage("OK - Se recuperaron el promedio y el porcentaje");
		} catch (EntityNotFoundException e) {
			log.error("Error al intentar recuperar el precio: ", e );
			averageAndPercentDiffResponse.setMessage(e.getMessage());
			return new ResponseEntity<BitcoinAveragePercentageResponse>(averageAndPercentDiffResponse, HttpStatus.NOT_FOUND);
		} catch (IllegalArgumentException e) {
			log.error("Error al intentar recuperar el precio: ", e );
			averageAndPercentDiffResponse.setMessage("Los datos de entrada son incorrectos");
			return new ResponseEntity<BitcoinAveragePercentageResponse>(averageAndPercentDiffResponse, HttpStatus.BAD_REQUEST);	
		} catch (Exception e) {
			log.error("Error al intentar recuperar el precio: ", e );
			averageAndPercentDiffResponse.setMessage(e.getMessage());
			return new ResponseEntity<BitcoinAveragePercentageResponse>(averageAndPercentDiffResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
		return new ResponseEntity<BitcoinAveragePercentageResponse>(averageAndPercentDiffResponse, HttpStatus.OK);
	}
	
}
