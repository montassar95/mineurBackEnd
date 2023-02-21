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
import com.cgpr.mineur.models.CauseDeces;
import com.cgpr.mineur.models.Etablissement;
 
import com.cgpr.mineur.repository.CauseDecesRepository;
 

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/causeDeces")
public class CauseDecesController {

	@Autowired
	private CauseDecesRepository causeDecesRepository;

	 
	 

	@GetMapping("/all")
	public ApiResponse<List<CauseDeces>> listCauseMutation() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.", causeDecesRepository.findAllByOrderByIdAsc());
	}

 

	@GetMapping("/getone/{id}")
	public ApiResponse<CauseDeces> getTypeAffaireById(@PathVariable("id") long id) {
		Optional<CauseDeces> typeData = causeDecesRepository.findById(id);
		if (typeData.isPresent()) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", typeData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "typeData Not FOund", null);
		}
	}

	@PostMapping("/add")
	public ApiResponse<CauseDeces> save(@RequestBody CauseDeces causeDeces) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "TypeAffaire saved Successfully",
					causeDecesRepository.save(causeDeces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<CauseDeces> update(@RequestBody CauseDeces causeDeces) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.",
					causeDecesRepository.save(causeDeces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "TypeAffaire not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			causeDecesRepository.deleteById(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "cause  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "cause not Deleted", null);
		}
	}
 
 
}
