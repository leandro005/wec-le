package com.lec.wenance.challenge.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lec.wenance.challenge.api.model.BitcoinPrice;
import com.lec.wenance.challenge.api.model.BitcoinPriceResponse;
import com.lec.wenance.challenge.api.repository.BitcoinRepository;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/wcl")
public class BitCoinPriceController {
	private static Logger log = LoggerFactory.getLogger(BitCoinPriceController.class);
	
	
	@Autowired
	private BitcoinRepository bitcoinRepository;
	
	@RequestMapping(value= "/findByTimestamp", method = RequestMethod.GET, produces = "application/json" )
	public @ResponseBody ResponseEntity<BitcoinPriceResponse> priceByTimestamp(@RequestParam("time") String time){
		BitcoinPriceResponse restResponse = new BitcoinPriceResponse();
		BitcoinPrice bitcoinPrice = null;
		try {
			bitcoinPrice = bitcoinRepository.findBitcoinPriceInTimestamp(Long.valueOf(time).longValue()).block();
			restResponse.setData(bitcoinPrice.getPrice().toString());
			restResponse.setMessage("OK con la busqueda");
		} catch (Exception e) {
			log.error("Error al intentar recuperar el precio: ", e );
			restResponse.setData("");
			restResponse.setMessage("Error al intentar recuperar el precio");
			return new ResponseEntity<BitcoinPriceResponse>(restResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
		return new ResponseEntity<BitcoinPriceResponse>(restResponse, HttpStatus.OK);
	}
	
	
	
}
