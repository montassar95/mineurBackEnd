package com.cgpr.mineur.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.dto.CarteHeberDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.service.CarteHeberService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/carteHeber")
public class CarteHeberController {

	@Autowired
	private CarteHeberService carteHeberService;

	@PostMapping("/add")
	public ApiResponse<CarteHeberDto> save(@RequestBody CarteHeberDto carteHeber) {

	 
		CarteHeberDto c = carteHeberService.save(carteHeber);

		return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully", c);

	}

}
