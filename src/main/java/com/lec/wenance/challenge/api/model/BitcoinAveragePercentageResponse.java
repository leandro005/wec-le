package com.lec.wenance.challenge.api.model;

import lombok.Data;

@Data
public class BitcoinAveragePercentageResponse {
	
	private String average;
	private String percentage;
	
	private String message;
}
