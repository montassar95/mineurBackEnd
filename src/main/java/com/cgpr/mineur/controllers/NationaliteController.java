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
import com.cgpr.mineur.models.Nationalite;
import com.cgpr.mineur.repository.NationaliteRepository;
import com.cgpr.mineur.service.NationaliteService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/nationalite")
public class NationaliteController {
	@Autowired
	private NationaliteService nationaliteService;

	@GetMapping("/all")
	public ApiResponse<List<Nationalite>> listNationalite() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.",
				nationaliteService.listNationalite());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<Nationalite> getNationaliteById(@PathVariable("id") long id) {
		 Nationalite  Data = nationaliteService.getNationaliteById(id);
	 
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", Data);
		 
	}

	@PostMapping("/add")
	public ApiResponse<Nationalite> save(@RequestBody Nationalite nationalite) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully",
					nationaliteService.save(nationalite));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "Nationalite not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<Nationalite> update(@RequestBody Nationalite nationalite) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.",
					nationaliteService.save(nationalite));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			nationaliteService.delete(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "   Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not Deleted", null);
		}
	}
}
