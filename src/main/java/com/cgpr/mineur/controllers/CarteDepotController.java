package com.cgpr.mineur.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.models.AccusationCarteDepot;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.CarteDepot;
import com.cgpr.mineur.repository.AccusationCarteDepotRepository;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.CarteDepotRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.service.CarteDepotService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/carteDepot")
public class CarteDepotController {

	@Autowired
	private CarteDepotService carteDepotService;
 
	
 
	
	
	
	
	
	
	
	@PostMapping("/add")
	public ApiResponse<CarteDepot> save(@RequestBody CarteDepot carteDepot) {

 
		 
		CarteDepot c = carteDepotService.save(carteDepot);
		 
		
  

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully", c);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}

	}

 
	
 
}
