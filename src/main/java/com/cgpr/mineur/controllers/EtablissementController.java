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

import com.cgpr.mineur.dto.EtablissementDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.service.EtablissementService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/etablissement")
public class EtablissementController {
	@Autowired
	private EtablissementService etablissementService;

	@GetMapping("/all")
	public ApiResponse<List<EtablissementDto>> listEtablissement() {

		return new ApiResponse<>(HttpStatus.OK.value(), "Etablissement List Fetched Successfully.",
				etablissementService.listEtablissement());
	}

	@GetMapping("/trouverEtablissementsActifs")
	public ApiResponse<List<EtablissementDto>> trouverEtablissementsActifs() {

		List<EtablissementDto> allCentre = etablissementService.listEtablissementCentre();
		return new ApiResponse<>(HttpStatus.OK.value(), "EtablissementCentre List Fetched Successfully.", allCentre);
	}

	@GetMapping("/getone/{id}")
	public ApiResponse<EtablissementDto> getEtablissementById(@PathVariable("id") String id) {
		EtablissementDto etablissementData = etablissementService.getEtablissementById(id);

		return new ApiResponse<>(HttpStatus.OK.value(), "Etablissement fetched suucessfully", etablissementData);

	}

	@PostMapping("/add")
	public ApiResponse<EtablissementDto> save(@RequestBody EtablissementDto etablissement) {

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "Etablissement saved Successfully",
					etablissementService.save(etablissement));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "Etablissement not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<EtablissementDto> update(@RequestBody EtablissementDto etablissement) {
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
