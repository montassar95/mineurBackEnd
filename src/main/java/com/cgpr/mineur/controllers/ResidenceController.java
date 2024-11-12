package com.cgpr.mineur.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.dto.ResidenceDto;
import com.cgpr.mineur.dto.ResidenceIdDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.service.ResidenceService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/residence")
public class ResidenceController {

	@Autowired
	private ResidenceService residenceService;

	
	@GetMapping("/validerNumeroEcrou/{numeroEcrou}/{etablissementId}")
	public ApiResponse<Boolean> validerAffaireNumeroEcrou(
	        @PathVariable("numeroEcrou") String numeroEcrou, 
	        @PathVariable("etablissementId") String etablissementId) {

	    try {
	        Boolean exist = residenceService.validerNumeroEcrou(numeroEcrou, etablissementId);
	        return new ApiResponse<>(HttpStatus.OK.value(), "Fetched successfully", exist);
	    } catch (Exception e) {
	        // Log the exception here
	        return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred", null);
	    }
	}


	
	
	
	@GetMapping("/trouverDerniereResidenceParNumDetentionEtIdDetenu/{idEnfant}/{numOrdinale}")
	public ApiResponse<ResidenceDto> trouverDerniereResidenceParNumDetentionEtIdDetenu(
			@PathVariable("idEnfant") String idEnfant, @PathVariable("numOrdinale") long numOrdinale) {

		ResidenceDto cData = residenceService.trouverDerniereResidenceParNumDetentionEtIdDetenu(idEnfant, numOrdinale);

		if (cData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", cData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  Not FOund", null);
		}
	}

	@GetMapping("/trouverResidencesDetentionActiveParIdDetenu/{idEnfant}")
	public ApiResponse<List<ResidenceDto>> trouverResidencesDetentionActiveParIdDetenu(
			@PathVariable("idEnfant") String idEnfant) {

		List<ResidenceDto> cData = residenceService.trouverResidencesDetentionActiveParIdDetenu(idEnfant);
		if (cData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", cData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  Not FOund", null);
		}
	}

	

	
	
	
	@PostMapping("/add")
	public ApiResponse<ResidenceDto> save(@RequestBody ResidenceDto residanceDto) {
		System.out.println(residanceDto.toString());

		try {
			ResidenceDto cData = residenceService.save(residanceDto);

			return new ApiResponse<>(HttpStatus.OK.value(), "Typeresidance saved Successfully", cData);

		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}
	}

	@PostMapping("/accepterDemandeMutation")
	public ApiResponse<ResidenceDto> accepterDemandeMutation(@RequestBody ResidenceDto residance) {

		ResidenceDto cData = residenceService.accepterDemandeMutation(residance);

		return new ApiResponse<>(HttpStatus.OK.value(), "Typeresidance saved Successfully", cData);

	}

	@PostMapping("/supprimerDemandeMutation")
	public ApiResponse<ResidenceDto> supprimerDemandeMutation(@RequestBody ResidenceIdDto residanceId) {

		try {
			residenceService.supprimerDemandeMutation(residanceId);

			return new ApiResponse<>(HttpStatus.OK.value(), "Typeresidance saved Successfully", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}
	}

	@PostMapping("/supprimerAcceptationMutation")
	public ApiResponse<ResidenceDto> supprimerAcceptationMutation(@RequestBody ResidenceIdDto residanceId) {

		try {

			ResidenceDto cData = residenceService.supprimerAcceptationMutation(residanceId);

			return new ApiResponse<>(HttpStatus.OK.value(), "Typeresidance saved Successfully", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}
	}

}
