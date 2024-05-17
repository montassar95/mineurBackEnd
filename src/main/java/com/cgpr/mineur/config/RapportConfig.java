package com.cgpr.mineur.config;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.RapportQuotidien;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.repository.RapportQuotidienRepository;
import com.cgpr.mineur.serviceReporting.ChargeAllEnfantService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Configuration
@EnableScheduling
public class RapportConfig {
	
	
	@Autowired
	private ChargeAllEnfantService chargeAllEnfantService;
	
	@Autowired
	private RapportQuotidienRepository rapportQuotidienRepository;
	
	@Scheduled(cron = "0 00 23 * * *")
	public void listCharge() {
		
		
		  ObjectMapper objectMapper = new ObjectMapper();
		//    objectMapper.setAnnotationIntrospector(new IgnoreJsonIgnoreIntrospector());
		String residenceJson = null;
		 
			try {
				residenceJson = objectMapper.writeValueAsString(chargeAllEnfantService.chargeList());
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				 
			}
		RapportQuotidien rapportQuotidien = new RapportQuotidien();
		rapportQuotidien.setDateSauvgarde(LocalDateTime.now());
		rapportQuotidien.setListResidance(residenceJson);
		
		rapportQuotidienRepository.save(rapportQuotidien);
		 
	}

}
