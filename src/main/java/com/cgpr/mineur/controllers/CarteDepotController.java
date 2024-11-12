package com.cgpr.mineur.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.dto.CarteDepotDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.service.CarteDepotService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/carteDepot")
public class CarteDepotController {

	@Autowired
	private CarteDepotService carteDepotService;

	@PostMapping("/add")
	public ApiResponse<CarteDepotDto> save(@RequestBody CarteDepotDto carteDepot) {

		CarteDepotDto c = carteDepotService.save(carteDepot);

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully", c);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}

	}

}
