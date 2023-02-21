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
import com.cgpr.mineur.models.CommentEchapper;
import com.cgpr.mineur.models.Deces;
import com.cgpr.mineur.models.Delegation;
import com.cgpr.mineur.repository.DelegationRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/delegation")
public class DelegationController {
	@Autowired
	private DelegationRepository delegationRepository;

	@GetMapping("/all")
	public ApiResponse<List<Delegation>> list() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.", delegationRepository.findAllByOrderByIdAsc());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<Delegation> getById(@PathVariable("id") long id) {
		Optional<Delegation> Data = delegationRepository.findById(id);
		if (Data.isPresent()) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", Data);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  Not FOund", null);
		}
	}

	@GetMapping("/getDelegationByGouv/{id}")
	public ApiResponse<List<Delegation>> getDelegationByGouv(@PathVariable("id") long id) {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.",
				delegationRepository.getDelegationByGouv(id));
	}

	@GetMapping("/findByGouvernorat/{idG}/{idD}")
	public ApiResponse<Delegation> findByGouvernorat(@PathVariable("idG") long idG, @PathVariable("idD") long idD) {

		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.",
				delegationRepository.findByGouvernorat(idG, idD));
	}

	@PostMapping("/add")
	public ApiResponse<Delegation> save(@RequestBody Delegation delegation) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "delegation saved Successfully",
					delegationRepository.save(delegation));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "delegation not saved", null);
		}
	}
	
	@PutMapping("/update")
	public ApiResponse<Delegation> update(@RequestBody Delegation causeDeces) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.",
					delegationRepository.save(causeDeces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "TypeAffaire not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			delegationRepository.deleteById(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "TypeAffaire  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not Deleted", null);
		}
	}
}
