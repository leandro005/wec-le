package com.lec.wenance.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.BodyContentSpec;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import com.lec.wenance.challenge.api.controller.BitCoinPriceController;
import com.lec.wenance.challenge.api.exception.EntityNotFoundException;
import com.lec.wenance.challenge.api.model.BitcoinPriceResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BitcoinPriceControllerTest {
	
	@Autowired
	private BitCoinPriceController controller;
	
	@Autowired
    private WebTestClient wtc;
	
	@Test
	public void priceByTimestampTest_OK() throws EntityNotFoundException {
		
		//Le damos tiempo a que se levanten los datos en el scheduler
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    
		ResponseEntity<BitcoinPriceResponse> response = null;
	    Timestamp timeNow = new Timestamp(new Date().getTime());
	    
		response = controller.priceByTimestamp(timeNow.toString());
		
		assertEquals(response.getStatusCode().value(), 200);
	}
	
	
	@Test
	public void priceByTimestampTest_validation_KO() throws EntityNotFoundException {

	    //Seteamos el input del request con datos erroneos
		ResponseEntity<BitcoinPriceResponse> response = controller.priceByTimestamp("119999999999999");
		
		assertNotEquals(response.getStatusCode().value(), 200);
		assertEquals(response.getStatusCode().value(), 400);
	}
	
	
	@Test
    void averageAndDiffPrice_OK() {
		
		//Damos tiempo a que se levanten los datos en memoria
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//Timestamp Actual
		Timestamp timeNow = new Timestamp(new Date().getTime());
		Calendar cal = Calendar.getInstance();
	    cal.setTimeInMillis(timeNow.getTime());
	 
	    // restamos 12 segundos
	    cal.add(Calendar.SECOND, -12);
	    Timestamp timestampFrom = new Timestamp(cal.getTime().getTime());
		
	    //Primero validamos el mensaje del output -> OK
		BodyContentSpec av = wtc.get().uri("/wcl/averageAndDiffPrice/"+timestampFrom.toString()+"/"+timeNow.toString())
	            .accept(MediaType.APPLICATION_JSON)
	            .exchange()
	            .expectStatus().isOk()
	            .expectBody()
	            .jsonPath("message").isEqualTo("OK - Se recuperaron el promedio y el porcentaje");
	    //Luego validamos el campo average no vacio 
	    av.jsonPath("average").isNotEmpty();
	    
    }
	
	
	@Test
    void averageAndDiffPrice_KO() {
		
		Timestamp timeNow = new Timestamp(new Date().getTime());
		Calendar cal = Calendar.getInstance();
	    cal.setTimeInMillis(timeNow.getTime());
	 
	    // restamos 2 minutos para dar mayor margen de error
	    cal.add(Calendar.MINUTE, -2);
	    timeNow = new Timestamp(cal.getTime().getTime());
		
	    //Validamos que el estado es NOT_FOUND
	    ResponseSpec av = wtc.get().uri("/wcl/averageAndDiffPrice/"+timeNow.toString()+"/"+timeNow.toString())
	            .accept(MediaType.APPLICATION_JSON)
	            .exchange()
	            .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
	    
	    //Luego validamos que el campo average viene vacio 
	    av.expectBody().jsonPath("average").isEmpty();
       
    }
	
}
