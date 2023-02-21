package com.cgpr.mineur.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.models.AccusationCarteDepot;
import com.cgpr.mineur.models.AccusationCarteDepotId;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.Arrestation;
import com.cgpr.mineur.models.ArrestationId;
import com.cgpr.mineur.models.ArretProvisoire;
import com.cgpr.mineur.models.CarteDepot;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.models.TitreAccusation;
import com.cgpr.mineur.repository.AccusationCarteDepotRepository;
import java.util.Date;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/accusationCarteDepot")
public class AccusationCarteDepotController {

	@Autowired
	private AccusationCarteDepotRepository accusationCarteDepotRepository;
 

 
	@PostMapping("/add")
	public ApiResponse<AccusationCarteDepot> save(@RequestBody AccusationCarteDepot accusationCarteDepot) {

	 
		 
 
		 
	 accusationCarteDepotRepository.save(accusationCarteDepot);

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}

	}
	


	@PostMapping("/findTitreAccusationbyCarteDepot")
	public ApiResponse<List<ArretProvisoire>> findTitreAccusationbyCarteDepot(@RequestBody  CarteDepot carteDepot) {
	
	 
	
	
	
	List<TitreAccusation> list = accusationCarteDepotRepository.getTitreAccusationbyDocument(carteDepot.getDocumentId());
	System.out.println(list.toString());
	if(list.isEmpty()) {
		
		
		return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not ", null);
		
		}
	else {
			try {
			return new ApiResponse<>(HttpStatus.OK.value(), "    Successfully", list);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not  ", null);
		}
	
	}
	
	
	}
 
}
