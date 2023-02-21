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
import com.cgpr.mineur.models.Etablissement;
import com.cgpr.mineur.models.NiveauEducatif;
import com.cgpr.mineur.repository.NiveauEducatifRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/niveauEducatif")
public class NiveauEducatifController {
	@Autowired
	private NiveauEducatifRepository niveauEducatifRepository;

	@GetMapping("/all")
	public ApiResponse<List<NiveauEducatif>> listNiveauEducatif() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.",
				niveauEducatifRepository.findAllByOrderByIdAsc());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<Etablissement> getNiveauEducatifById(@PathVariable("id") long id) {
		Optional<NiveauEducatif> Data = niveauEducatifRepository.findById(id);
		if (Data.isPresent()) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", Data);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  Not FOund", null);
		}
	}

	@PostMapping("/add")
	public ApiResponse<NiveauEducatif> save(@RequestBody NiveauEducatif niveauEducatif) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully",
					niveauEducatifRepository.save(niveauEducatif));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "Etablissement not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<NiveauEducatif> update(@RequestBody NiveauEducatif niveauEducatif) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.",
					niveauEducatifRepository.save(niveauEducatif));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			niveauEducatifRepository.deleteById(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "   Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not Deleted", null);
		}
	}
}
