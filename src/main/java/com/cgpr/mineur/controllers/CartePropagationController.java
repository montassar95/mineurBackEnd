package com.cgpr.mineur.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.dto.CartePropagationDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.service.CartePropagationService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cartePropagation")
public class CartePropagationController {

	@Autowired
	private CartePropagationService cartePropagationService;

	@PostMapping("/add")
	public ApiResponse<CartePropagationDto> save(@RequestBody CartePropagationDto cartePropagation) {

		CartePropagationDto c = cartePropagationService.save(cartePropagation);

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully", c);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}

	}

}
