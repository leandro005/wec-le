package com.lec.wenance.challenge.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lec.wenance.challenge.api.model.BitcoinPrice;
import com.lec.wenance.challenge.api.repository.BitcoinRepository;
import com.lec.wenance.challenge.scheduler.dto.CEXLastPrice;
import com.lec.wenance.challenge.scheduler.services.CEXLastPriceService;

@Component
public class CEXApiReadingScheduler {
	
	private static final Logger log = LoggerFactory.getLogger(CEXApiReadingScheduler.class); 
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	@Autowired
	private CEXLastPriceService cexLastPriceService;
		
	@Autowired
	private BitcoinRepository bitcoinRepository;
	
	@Scheduled(fixedDelay = 10000 )
    public void scheduled() {
		
		CEXLastPrice cexLastPriceResponse = cexLastPriceService.getCEXLastPriceResponse();
		log.info("CEXLastPriceResponse ->>>  lprice: {}, curr1: {}, curr2: {}, date: {}.",
				cexLastPriceResponse.getLprice(),
				cexLastPriceResponse.getCurr1(),
				cexLastPriceResponse.getCurr2(),
				dateFormat.format(new Date().getTime()) );
		
		bitcoinRepository.insertBitcoinPrice(new BitcoinPrice(cexLastPriceResponse));
		
    }
	 
}
