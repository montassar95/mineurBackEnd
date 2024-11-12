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

import com.cgpr.mineur.dto.AffaireData;
import com.cgpr.mineur.dto.AffaireDto;
import com.cgpr.mineur.dto.FicheDeDetentionDto;
import com.cgpr.mineur.dto.VerifierAffaireDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.service.AffaireService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/affaire")
public class AffaireController {

	@Autowired
	private AffaireService affaireService;

	@GetMapping("/calculerDateFin/{date}/{duree}")
	public ApiResponse<Object> calculerDateFin(@PathVariable("date") String date, @PathVariable("duree") int duree) {

		String dateDtring = "";

		dateDtring = (String) affaireService.calculerDateFin(date, duree);

		return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", dateDtring);

	}

	 

	@GetMapping("/obtenirInformationsDeDetentionParIdDetention/{idEnfant}/{numOrdinale}")
	public ApiResponse<FicheDeDetentionDto> obtenirInformationsDeDetentionParIdDetention(
			@PathVariable("idEnfant") String idEnfant, @PathVariable("numOrdinale") long numOrdinale) {

		FicheDeDetentionDto output = affaireService.obtenirInformationsDeDetentionParIdDetention(idEnfant, numOrdinale);

		return new ApiResponse<>(HttpStatus.ACCEPTED.value(), "okk", output);

	}

	@GetMapping("/trouverAffairesParAction/{action}/{idEnfant}/{numOrdinale}")
	public ApiResponse<List<AffaireDto>> trouverAffairesParAction(@PathVariable("action") String action,
			@PathVariable("idEnfant") String idEnfant, @PathVariable("numOrdinale") long numOrdinale) {

		List<AffaireDto> output = affaireService.trouverAffairesParAction(action, idEnfant, numOrdinale);
		return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "a Not FOund", output);

	}

	@PostMapping("/mettreAJourNumeroOrdinal")
	public ApiResponse<AffaireDto> mettreAJourNumeroOrdinal(@RequestBody AffaireDto affaire ) {

		try {
			affaire = affaireService.mettreAJourNumeroOrdinal(affaire );

			return new ApiResponse<>(HttpStatus.OK.value(), " Affaire saved Successfully", affaire);

		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}
	}

	@PostMapping("/validerAffaire")
	public ApiResponse<VerifierAffaireDto> validerAffaire(@RequestBody AffaireData affaireData) {
		VerifierAffaireDto erifierAffaireDto = affaireService.validerAffaire(affaireData);

		return new ApiResponse<>(HttpStatus.OK.value(), " verifierAffaireDto", erifierAffaireDto);

	}

}
