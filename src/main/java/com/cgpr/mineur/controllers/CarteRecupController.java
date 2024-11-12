package com.cgpr.mineur.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.dto.CarteRecupDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.service.CarteRecupService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/carteRecup")
public class CarteRecupController {

	@Autowired
	private CarteRecupService carteRecupService;

	@PostMapping("/add")
	public ApiResponse<CarteRecupDto> save(@RequestBody CarteRecupDto carteRecup) {

		CarteRecupDto c = carteRecupService.save(carteRecup);

		return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully", c);

	}

}
