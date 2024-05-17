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

import com.cgpr.mineur.models.TitreAccusation;
import com.cgpr.mineur.repository.TitreAccusationRepository;
import com.cgpr.mineur.service.TitreAccusationService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/titreAccusation")
public class TitreAccusationController {
	@Autowired
	private TitreAccusationService titreAccusationServicey;

	@GetMapping("/findTitreAccusationByIdTypeAffaire/{id}")
	public ApiResponse<List<TitreAccusation>> findTitreAccusationByIdTypeAffaire(@PathVariable("id") long id) {
		List<TitreAccusation> accusationData = titreAccusationServicey.findTitreAccusationByIdTypeAffaire(id);
		if (accusationData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "accusationData fetched suucessfully", accusationData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "accusationData Not FOund", null);
		}
	}

	@GetMapping("/all")
	public ApiResponse<List<TitreAccusation>> list() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.",
				titreAccusationServicey.list());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<TitreAccusation> getById(@PathVariable("id") long id) {
		 TitreAccusation  Data = titreAccusationServicey.getById(id);
		 
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", Data);
		 
	}

	@PostMapping("/add")
	public ApiResponse<TitreAccusation> save(@RequestBody TitreAccusation causeDeces) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "TypeAffaire saved Successfully", titreAccusationServicey.save(causeDeces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<TitreAccusation> update(@RequestBody TitreAccusation causeDeces) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.",
					titreAccusationServicey.save(causeDeces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "TypeAffaire not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			titreAccusationServicey.delete(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "TypeAffaire  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not Deleted", null);
		}
	}

}
