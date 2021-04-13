package com.lec.wenance.challenge.api.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.lec.wenance.challenge.scheduler.dto.CEXLastPrice;

import lombok.Data;

@Data
public class BitcoinPrice {
	
	public BitcoinPrice(CEXLastPrice dto) {
		this.price = dto.getLprice();
		this.timestamp = new Date().getTime() ;
	}
	
	private long timestamp;
	private BigDecimal price; 
	
	
}
