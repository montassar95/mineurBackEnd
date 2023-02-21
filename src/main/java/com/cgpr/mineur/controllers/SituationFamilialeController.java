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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/situationFamiliale")
public class SituationFamilialeController {
	@Autowired
	private SituationFamilialeRepository situationFamilialeRepository;

	@GetMapping("/all")
	public ApiResponse<List<SituationFamiliale>> listNationalite() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.",
				situationFamilialeRepository.findAllByOrderByIdAsc());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<SituationFamiliale> getById(@PathVariable("id") long id) {
		Optional<SituationFamiliale> Data = situationFamilialeRepository.findById(id);
		if (Data.isPresent()) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", Data);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  Not FOund", null);
		}
	}

	@PostMapping("/add")
	public ApiResponse<SituationFamiliale> save(@RequestBody SituationFamiliale situationFamiliale) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully",
					situationFamilialeRepository.save(situationFamiliale));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<SituationFamiliale> update(@RequestBody SituationFamiliale situationFamiliale) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.",
					situationFamilialeRepository.save(situationFamiliale));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			situationFamilialeRepository.deleteById(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "   Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not Deleted", null);
		}
	}
}
