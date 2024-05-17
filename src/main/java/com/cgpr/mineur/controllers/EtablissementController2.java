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
import com.cgpr.mineur.repository.EtablissementRepository;
import com.cgpr.mineur.service.EtablissementService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/etablissement")
public class EtablissementController2 {
	@Autowired
	private EtablissementService etablissementService;

	@GetMapping("/all")
	public ApiResponse<List<Etablissement>> listEtablissement() {
		
		return new ApiResponse<>(HttpStatus.OK.value(), "Etablissement List Fetched Successfully.",
				etablissementService.listEtablissement());
	}
	@GetMapping("/allCentre")
	public ApiResponse<List<Etablissement>> listEtablissementCentre() {
		
		List<Etablissement> allCentre =  etablissementService.listEtablissementCentre();
		return new ApiResponse<>(HttpStatus.OK.value(), "EtablissementCentre List Fetched Successfully.",
				allCentre);
	}
	@GetMapping("/getone/{id}")
	public ApiResponse<Etablissement> getEtablissementById(@PathVariable("id") String id) {
		 Etablissement  etablissementData = etablissementService.getEtablissementById(id);
		 
			return new ApiResponse<>(HttpStatus.OK.value(), "Etablissement fetched suucessfully", etablissementData);
		 
	}

	@PostMapping("/add")
	public ApiResponse<Etablissement> save(@RequestBody Etablissement etablissement) {
		 
		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "Etablissement saved Successfully",
					etablissementService.save(etablissement));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "Etablissement not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<Etablissement> update(@RequestBody Etablissement etablissement) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "etablissement updated successfully.",
					etablissementService.save(etablissement));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "etablissement not Saved", null);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ApiResponse<Void> delete(@PathVariable("id") String id) {
		try {
			etablissementService.delete(id);
			return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "etablissement  Deleted", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "etablissement not Deleted", null);
		}
	}
}
