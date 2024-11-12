package com.cgpr.mineur.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.dto.ArrestationDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.service.ArrestationService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/arrestation")
public class ArrestationController {
	@Autowired
	private ArrestationService arrestationService;

	@GetMapping("/trouverDerniereDetentionParIdDetenu/{id}")
	public ApiResponse<ArrestationDto> trouverDerniereDetentionParIdDetenu(@PathVariable("id") String id) {

		ArrestationDto arrestationData = arrestationService.trouverDerniereDetentionParIdDetenu(id);

		return new ApiResponse<>(HttpStatus.OK.value(), "arrestationData fetched suucessfully", arrestationData);

	}

	@GetMapping("/calculerNombreDetentionsParIdDetenu/{id}")
	public ApiResponse<Object> calculerNombreDetentionsParIdDetenu(@PathVariable("id") String id) {

		return new ApiResponse<>(HttpStatus.OK.value(), "ok", arrestationService.calculerNombreDetentionsParIdDetenu(id));

	}

	@PostMapping("/add")
	public ApiResponse<ArrestationDto> save(@RequestBody ArrestationDto arrestation) {

		ArrestationDto a = arrestationService.save(arrestation);

		return new ApiResponse<>(HttpStatus.OK.value(), "Arrestation saved Successfully", a);

	}

	@PostMapping("/supprimerLiberation")
	public ApiResponse<ArrestationDto> supprimerLiberation(@RequestBody ArrestationDto arrestation) {

		ArrestationDto a = arrestationService.delete(arrestation);

		return new ApiResponse<>(HttpStatus.OK.value(), "Arrestation saved Successfully", a);

	}

}
