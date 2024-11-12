package com.cgpr.mineur.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.dto.EnfantDto;
import com.cgpr.mineur.dto.EnfantVerifieDto;
import com.cgpr.mineur.dto.ResidenceDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.repository.RapportEnfantQuotidienRepository;
import com.cgpr.mineur.resource.EnfantAddDTO;
import com.cgpr.mineur.resource.EnfantDTO;
import com.cgpr.mineur.resource.PDFListExistDTO;
import com.cgpr.mineur.resource.PDFPenaleDTO;
import com.cgpr.mineur.service.EnfantService;
import com.cgpr.mineur.serviceReporting.ChargeAllEnfantService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/enfant")
public class EnfantController {
	@Autowired
	private EnfantService enfantService;

 
 
	
	
	
	
	
	
	@PostMapping("/trouverResidencesParCriteresDetenu")
	public ApiResponse<List<ResidenceDto>> trouverResidencesParCriteresDetenu(@RequestBody EnfantDTO enfantDTO) {

		List<ResidenceDto> enfantData = enfantService.trouverResidencesParCriteresDetenu(enfantDTO);

		if (enfantData != null) {

			return new ApiResponse<>(HttpStatus.OK.value(), "enfantData fetched suucessfully", enfantData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "enfantData Not FOund", null);
		}
	}

	

	

	@GetMapping("/getone/{id}")
	public ApiResponse<EnfantDto> getEnfantById(@PathVariable("id") String id) {
		EnfantDto enfanttData = enfantService.getEnfantById(id);
		return new ApiResponse<>(HttpStatus.OK.value(), "Enfant fetched suucessfully", enfanttData);
	}

	@GetMapping("/trouverDetenuAvecSonStatutActuel/{id}/{idEtab}")
	public ApiResponse<EnfantVerifieDto> trouverDetenuAvecSonStatutActuel(@PathVariable("id") String id,
			@PathVariable("idEtab") String idEtab) {
		EnfantVerifieDto enfanttData = enfantService.trouverDetenuAvecSonStatutActuel(id, idEtab);

		return new ApiResponse<>(HttpStatus.OK.value(), "Enfant fetched suucessfully", enfanttData);
	}

	@GetMapping("/trouverDerniereResidenceParIdDetenu/{id}")
	public ApiResponse<ResidenceDto> trouverDerniereResidenceParIdDetenu(@PathVariable("id") String id) {

		ResidenceDto enfantData = enfantService.trouverDerniereResidenceParIdDetenu(id);

		return new ApiResponse<>(HttpStatus.OK.value(), "Enfant fetched suucessfully", enfantData);

	}

	@GetMapping("/trouverResidencesParNumeroEcrou/{numArr}")
	public ApiResponse<List<ResidenceDto>> trouverResidencesParNumeroEcrou(@PathVariable("numArr") String numArr) {
		List<ResidenceDto> enfantData = enfantService.trouverResidencesParNumeroEcrou(numArr);

		if (enfantData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "Enfant fetched suucessfully", enfantData);
		}

		else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Enfant Not FOund", null);
		}
	}

	@PostMapping("/creerAdmissionDetenu")
	public ApiResponse<ResidenceDto> creerAdmissionDetenu(@RequestBody EnfantAddDTO enfantAddDTO) {
		try {

			ResidenceDto newResidence = enfantService.creerAdmissionDetenu(enfantAddDTO);

			System.err.println(newResidence.toString());

			return new ApiResponse<>(HttpStatus.OK.value(), "Enfant saved Successfully", newResidence);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "Enfant not saved", null);
		}

	}

	@PostMapping("/mettreAJourAdmissionDetenu")
	public ApiResponse<ResidenceDto> mettreAJourAdmissionDetenu(@RequestBody EnfantAddDTO enfantAddDTO) {

		ResidenceDto newResidence = enfantService.mettreAJourAdmissionDetenu(enfantAddDTO);

		return new ApiResponse<>(HttpStatus.OK.value(), "Enfant saved Successfully", newResidence);

	}

	 

}
