package com.cgpr.mineur.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.models.AccusationCarteRecup;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.service.AccusationCarteRecupService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/accusationCarteRecup")
public class AccusationCarteRecupController {

	 
	@Autowired
	private AccusationCarteRecupService accusationCarteRecupService;

	@PostMapping("/findByCarteRecup")
	public ApiResponse<List<AccusationCarteRecup>> findByCarteRecup(@RequestBody CarteRecup carteRecup) {

	 

		List<AccusationCarteRecup> list = accusationCarteRecupService.findByCarteRecup(carteRecup );
		 
		if (list.isEmpty()) {

			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not ", null);

		} else {
			try {
				return new ApiResponse<>(HttpStatus.OK.value(), "    Successfully", list);
			} catch (Exception e) {
				return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not  ", null);
			}

		}

	}

	@GetMapping("/calcule/{date}/{duree}")
	public ApiResponse<Object> getArrestationById(@PathVariable("date") String date, @PathVariable("duree") int duree) {

	 	String dateDtring = "";

		 
			 
			dateDtring = (String) accusationCarteRecupService.getArrestationById(date , duree);
			 
		 
		return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", dateDtring);

	}

	@PostMapping("/add")
	public ApiResponse<AccusationCarteRecup> save(@RequestBody AccusationCarteRecup accusationCarteRecup) {

		accusationCarteRecupService.save(accusationCarteRecup);

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}

	}

}
