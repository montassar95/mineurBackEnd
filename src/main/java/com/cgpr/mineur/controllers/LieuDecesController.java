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
import com.cgpr.mineur.models.CauseMutation;
 
import com.cgpr.mineur.models.LieuDeces;
import com.cgpr.mineur.repository.CauseMutationRepository;
import com.cgpr.mineur.repository.LieuDecesRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/lieuDeces")
public class LieuDecesController {

	@Autowired
	private LieuDecesRepository lieuDecesRepository;

	 
	 

	@GetMapping("/all")
	public ApiResponse<List<LieuDeces>> listCauseMutation() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.", lieuDecesRepository.findAllByOrderByIdAsc());
	}

 
	@GetMapping("/getone/{id}")
	public ApiResponse<LieuDeces> getById(@PathVariable("id") long id) {
		Optional<LieuDeces> Data = lieuDecesRepository.findById(id);
		if (Data.isPresent()) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", Data);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  Not FOund", null);
		}
	}
	
	
 

	@PostMapping("/add")
	public ApiResponse<LieuDeces> save(@RequestBody LieuDeces causeDeces) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "TypeAffaire saved Successfully",
					lieuDecesRepository.save(causeDeces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<LieuDeces> update(@RequestBody LieuDeces causeDeces) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.",
					lieuDecesRepository.save(causeDeces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "TypeAffaire not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			lieuDecesRepository.deleteById(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "TypeAffaire  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not Deleted", null);
		}
	}
 
 
}
