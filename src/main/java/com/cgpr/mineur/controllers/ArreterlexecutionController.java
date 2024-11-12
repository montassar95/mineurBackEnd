package com.cgpr.mineur.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.dto.ArreterlexecutionDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.service.ArreterlexecutionService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/arreterlexecution")
public class ArreterlexecutionController {

	@Autowired
	private ArreterlexecutionService arreterlexecutionService;

	@PostMapping("/add")
	public ApiResponse<ArreterlexecutionDto> save(@RequestBody ArreterlexecutionDto arreterlexecution) {

		arreterlexecutionService.save(arreterlexecution);
		return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully", null);

	}

}
