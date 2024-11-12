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

import com.cgpr.mineur.dto.EchappesDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.service.EchappesService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/echappes")
public class EchappesController {

	@Autowired
	private EchappesService echappesService;
 

	@PostMapping("/add")
	public ApiResponse<EchappesDto> save(@RequestBody EchappesDto echappes) {

		System.out.print(echappes .toString());

		try {
 
			EchappesDto e = echappesService.save(echappes);

			return new ApiResponse<>(HttpStatus.OK.value(), "Typeresidance saved Successfully", e);

		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}
	}

	 

	 

 

	@GetMapping("/trouverEchappesParIdDetenuEtNumDetention /{idEnfant}/{numOrdinaleArrestation}")
	public ApiResponse<List<EchappesDto>> trouverEchappesParIdDetenuEtNumDetention (@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinaleArrestation") long numOrdinaleArrestation) {

		List<EchappesDto> cData = echappesService.trouverEchappesParIdDetenuEtNumDetention(idEnfant, numOrdinaleArrestation);
		if (cData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", cData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  Not FOund", null);
		}
	}

}
