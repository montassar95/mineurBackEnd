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
 
import com.cgpr.mineur.models.MotifArreterlexecution;
import com.cgpr.mineur.repository.MotifArreterlexecutionRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/motifArreterlexecution")
public class MotifArreterlexecutionController {

	@Autowired
	private MotifArreterlexecutionRepository motifArreterlexecutionRepository;

	 
	 

	@GetMapping("/all")
	public ApiResponse<List<MotifArreterlexecution>> listMotifArreterlexecution() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.", motifArreterlexecutionRepository.findAllByOrderByIdAsc());
	}

 
	@GetMapping("/getone/{id}")
	public ApiResponse<MotifArreterlexecution> getById(@PathVariable("id") long id) {
		Optional<MotifArreterlexecution> Data = motifArreterlexecutionRepository.findById(id);
		if (Data.isPresent()) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", Data);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  Not FOund", null);
		}
	}
	
	
	 
	
 

	@PostMapping("/add")
	public ApiResponse<MotifArreterlexecution> save(@RequestBody MotifArreterlexecution causeDeces) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "TypeAffaire saved Successfully",
					motifArreterlexecutionRepository.save(causeDeces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<MotifArreterlexecution> update(@RequestBody MotifArreterlexecution causeDeces) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.",
					motifArreterlexecutionRepository.save(causeDeces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "TypeAffaire not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			motifArreterlexecutionRepository.deleteById(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "TypeAffaire  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not Deleted", null);
		}
	}
 
 
 
}