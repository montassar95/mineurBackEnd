package com.cgpr.mineur.controllers;

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

import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.Liberation;
import com.cgpr.mineur.models.LiberationId;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.LiberationRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/liberation")
public class LiberationController {

	@Autowired
	private LiberationRepository liberationRepository;

 
	
	@Autowired
	private ArrestationRepository arrestationRepository;
	 

	@GetMapping("/all")
	public ApiResponse<List<Liberation>> listLiberation() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.", liberationRepository.findAll());
	}

 
//	@PostMapping("/add")
//	public ApiResponse<Liberation> save(@RequestBody Liberation liberation) {
//
//		try {
//		 
//		
//			
//			arrestationRepository.save(liberation.getArrestation());
//		
//			
// 			return new ApiResponse<>(HttpStatus.OK.value(), "Liberation saved Successfully",
// 					liberationRepository.save(liberation));
//		} catch (Exception e) {
//			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "Arrestation not saved", null);
//		}
//	}
	
	@GetMapping("/getLiberationById/{idEnfant}/{numOrdinale}")
	public ApiResponse<Liberation> getLiberationById(@PathVariable("idEnfant") String idEnfant,
			                                         @PathVariable("numOrdinale") long numOrdinale) {
		
		LiberationId liberationId = new LiberationId(idEnfant,numOrdinale);
		Optional<Liberation> cData = liberationRepository.findById(liberationId);
		if (cData.isPresent()) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", cData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  Not FOund", null);
		}
	}
 
}
