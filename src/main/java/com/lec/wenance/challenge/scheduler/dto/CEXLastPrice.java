package com.lec.wenance.challenge.scheduler.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CEXLastPrice {
	
	private BigDecimal lprice;
	private String curr1;
	private String curr2;
	
}
