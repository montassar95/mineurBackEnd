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

import com.cgpr.mineur.models.SituationFamiliale;
import com.cgpr.mineur.repository.SituationFamilialeRepository;
import com.cgpr.mineur.service.SituationFamilialeService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/situationFamiliale")
public class SituationFamilialeController {
	@Autowired
	private SituationFamilialeService situationFamilialeService;

	@GetMapping("/all")
	public ApiResponse<List<SituationFamiliale>> listNationalite() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.",
				situationFamilialeService.listNationalite());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<SituationFamiliale> getById(@PathVariable("id") long id) {
		 SituationFamiliale  Data = situationFamilialeService.getById(id);
		 
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", Data);
		 
	}

	@PostMapping("/add")
	public ApiResponse<SituationFamiliale> save(@RequestBody SituationFamiliale situationFamiliale) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully",
					situationFamilialeService.save(situationFamiliale));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<SituationFamiliale> update(@RequestBody SituationFamiliale situationFamiliale) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.",
					situationFamilialeService.update(situationFamiliale));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			situationFamilialeService.delete(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "   Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not Deleted", null);
		}
	}
}
