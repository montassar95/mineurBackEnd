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

import com.cgpr.mineur.models.Metier;

import com.cgpr.mineur.repository.MetierRepository;
import com.cgpr.mineur.service.MetierService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/metier")
public class MetierController {
	@Autowired
	private MetierService  metierService;

	@GetMapping("/all")
	public ApiResponse<List<Metier>> list() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.",
				metierService.list());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<Metier> getById(@PathVariable("id") long id) {
		 Metier  Data = metierService.getById(id);
		 
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", Data);
		 
	}

	
	
	
	@PostMapping("/add")
	public ApiResponse<Metier> save(@RequestBody Metier gouv) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "TypeAffaire saved Successfully",
					metierService.save(gouv));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<Metier> update(@RequestBody Metier gouv) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.",
					metierService.save(gouv));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "TypeAffaire not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			metierService.delete(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "TypeAffaire  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not Deleted", null);
		}
	}
 
}
