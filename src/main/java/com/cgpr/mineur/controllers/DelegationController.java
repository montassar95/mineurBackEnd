package com.cgpr.mineur.controllers;

import java.util.List;

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

import com.cgpr.mineur.dto.DelegationDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.service.DelegationService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/delegation")
public class DelegationController {
	@Autowired
	private DelegationService delegationService;

	@GetMapping("/all")
	public ApiResponse<List<DelegationDto>> list() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.", delegationService.list());
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<DelegationDto> getById(@PathVariable("id") long id) {
		DelegationDto Data = delegationService.getById(id);

		return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", Data);

	}

	@GetMapping("/trouverDelegationsParGouvernorat/{id}")
	public ApiResponse<List<DelegationDto>> trouverDelegationsParGouvernorat(@PathVariable("id") long id) {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.",
				delegationService.getDelegationByGouv(id));
	}

	@GetMapping("/trouverDelegationParIdDelegationEtIdGouvernorat/{idG}/{idD}")
	public ApiResponse<DelegationDto> trouverDelegationParIdDelegationEtIdGouvernorat(@PathVariable("idG") long idG,
			@PathVariable("idD") long idD) {

		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.",
				delegationService.findByGouvernorat(idG, idD));
	}

	@PostMapping("/add")
	public ApiResponse<DelegationDto> save(@RequestBody DelegationDto delegation) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "delegation saved Successfully",
					delegationService.save(delegation));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "delegation not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<DelegationDto> update(@RequestBody DelegationDto causeDeces) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.",
					delegationService.save(causeDeces));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "TypeAffaire not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") long id) {
		try {
			delegationService.delete(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "TypeAffaire  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "TypeAffaire not Deleted", null);
		}
	}
}
