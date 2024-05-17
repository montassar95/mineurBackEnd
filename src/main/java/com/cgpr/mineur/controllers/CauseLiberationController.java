package com.cgpr.mineur.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.models.ApiResponse;
 
import com.cgpr.mineur.models.CauseLiberation;
import com.cgpr.mineur.repository.CauseLiberationRepository;
import com.cgpr.mineur.service.CauseLiberationService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/causeLiberation")
public class CauseLiberationController {

	@Autowired
	private CauseLiberationService causeLiberationService;

	 
	 

	@GetMapping("/all")
	public ApiResponse<List<CauseLiberation>> listCauseLiberation() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.", causeLiberationService.listCauseLiberation());
	}


	@GetMapping("/getone/{id}")
	public ApiResponse<CauseLiberation> getTypeAffaireById(@PathVariable("id") long id) {
		
		 CauseLiberation  typeData =causeLiberationService.getTypeAffaireById(id);
		 	return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", typeData);
		 
	}

	@PostMapping("/add")
	public ApiResponse<CauseLiberation> save(@RequestBody CauseLiberation causeDeces) {

		 	return new ApiResponse<>(HttpStatus.OK.value(), "TypeAffaire saved Successfully",
		 			causeLiberationService.save(causeDeces));
		 
	}

	@PutMapping("/update")
	public ApiResponse<CauseLiberation> update(@RequestBody CauseLiberation causeDeces) {
		 

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.",
					causeLiberationService.save(causeDeces));
		 

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		 
		  causeLiberationService.delete(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "TypeAffaire  Deleted", null);
		 
	}
 
 
 
}
