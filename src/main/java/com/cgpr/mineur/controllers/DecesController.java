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
 
import com.cgpr.mineur.models.Deces;
import com.cgpr.mineur.models.Enfant;
import com.cgpr.mineur.models.Gouvernorat;
import com.cgpr.mineur.repository.DecesRepository;
import com.cgpr.mineur.repository.GouvernoratRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/deces")
public class DecesController {
	@Autowired
	private DecesRepository decesRepository;

	@GetMapping("/all")
	public ApiResponse<List<Deces>> list() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.",
				decesRepository.findAll());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<Deces> getById(@PathVariable("id") long id) {
		Optional<Deces> Data = decesRepository.findById(id);
		if (Data.isPresent()) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", Data);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  Not FOund", null);
		}
	}
	
	@PostMapping("/add")
	public ApiResponse<Deces> save(@RequestBody Deces deces) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "Enfant saved Successfully", decesRepository.save(deces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "Enfant not saved", null);
		}
	}
	
	

	 

	 

	@PutMapping("/update")
	public ApiResponse<Deces> update(@RequestBody Deces causeDeces) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.",
					decesRepository.save(causeDeces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "TypeAffaire not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			decesRepository.deleteById(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "TypeAffaire  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not Deleted", null);
		}
	}

}
