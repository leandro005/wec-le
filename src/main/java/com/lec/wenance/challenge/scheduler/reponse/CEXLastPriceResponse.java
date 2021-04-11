package com.lec.wenance.challenge.scheduler.reponse;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CEXLastPriceResponse {
	
	private BigDecimal lprice;
	private String curr1;
	private String curr2;
	
}
