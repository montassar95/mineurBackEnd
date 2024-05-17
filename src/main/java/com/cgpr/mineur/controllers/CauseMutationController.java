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
import com.cgpr.mineur.repository.CauseMutationRepository;
import com.cgpr.mineur.service.CauseMutationService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/causeMutation")
public class CauseMutationController {

	@Autowired
	private CauseMutationService  causeMutationService;

	 
	 

	@GetMapping("/all")
	public ApiResponse<List<CauseMutation>> listCauseMutation() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.", causeMutationService.listCauseMutation());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<CauseMutation> getTypeAffaireById(@PathVariable("id") long id) {
		 CauseMutation  typeData = causeMutationService.getTypeAffaireById(id);
		 
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", typeData);
		 
	}

	@PostMapping("/add")
	public ApiResponse<CauseMutation> save(@RequestBody CauseMutation causeDeces) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "TypeAffaire saved Successfully",
					causeMutationService.save(causeDeces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<CauseMutation> update(@RequestBody CauseMutation causeDeces) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.",
					causeMutationService.save(causeDeces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "TypeAffaire not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			causeMutationService.delete(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "TypeAffaire  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not Deleted", null);
		}
	}
 
 
 
}
