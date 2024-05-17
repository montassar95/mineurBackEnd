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
import com.cgpr.mineur.models.Gouvernorat;
 
import com.cgpr.mineur.repository.GouvernoratRepository;
import com.cgpr.mineur.service.GouvernoratService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/gouvernorat")
public class GouvernoratController {
	@Autowired
	private GouvernoratService gouvernoratService;

	@GetMapping("/all")
	public ApiResponse<List<Gouvernorat>> list() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.",
				gouvernoratService.list());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<Gouvernorat> getById(@PathVariable("id") long id) {
		 Gouvernorat  Data = gouvernoratService.getById(id);
		 
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", Data);
		 
	}

	
	
	
	@PostMapping("/add")
	public ApiResponse<Gouvernorat> save(@RequestBody Gouvernorat gouv) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "TypeAffaire saved Successfully",
					gouvernoratService.save(gouv));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<Gouvernorat> update(@RequestBody Gouvernorat gouv) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.", gouvernoratService.save(gouv));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "TypeAffaire not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			gouvernoratService.delete(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "TypeAffaire  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not Deleted", null);
		}
	}
 
}
