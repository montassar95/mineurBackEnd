package com.cgpr.mineur.controllers;

import java.sql.Date;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.dto.CalculeAffaireDto;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.AffaireId;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.Arreterlexecution;
import com.cgpr.mineur.models.CarteDepot;
import com.cgpr.mineur.models.CarteHeber;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.models.Document;
import com.cgpr.mineur.models.Revue;
import com.cgpr.mineur.models.TitreAccusation;
import com.cgpr.mineur.models.Transfert;
import com.cgpr.mineur.repository.AccusationCarteDepotRepository;
import com.cgpr.mineur.repository.AccusationCarteHeberRepository;
import com.cgpr.mineur.repository.AccusationCarteRecupRepository;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.service.AffaireService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/affaire")
public class AffaireController {

	@Autowired
	private AffaireService affaireService;
 
	@GetMapping("/all")
	public ApiResponse<List<Affaire>> listAffaire() {
		return new ApiResponse<>(HttpStatus.OK.value(), "Etablissement List Fetched Successfully.",
				affaireService.listAffaire());
	}

	@GetMapping("/getAffaireById/{idEnfant}/{numAffaire}/{idTribunal}/{numOrdinaleArrestation}" )
	public ApiResponse<Affaire> getAffaireById(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numAffaire") String numAffaire, @PathVariable("idTribunal") long idTribunal,
			@PathVariable("numOrdinaleArrestation") long numOrdinaleArrestation) {
		 
		 
		Affaire aData = affaireService.getAffaireById(idEnfant, numAffaire, idTribunal, numOrdinaleArrestation);
		 
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", aData);
	 
	}

	@GetMapping("/findAffaireByAnyArrestation/{idEnfant}/{numAffaire}/{idTribunal}")
	public ApiResponse<List<Affaire>> findAffaireByAnyArrestation(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numAffaire") String numAffaire, @PathVariable("idTribunal") long idTribunal) {

		List<Affaire> aData = affaireService.findAffaireByAnyArrestation(idEnfant, numAffaire, idTribunal);
		if (aData.isEmpty()) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Not Found", null);
		} else {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", aData);

		}
	}

	@GetMapping("/findAffaireByAffaireLien/{idEnfant}/{numAffaire}/{idTribunal}")
	public ApiResponse<Affaire> findAffaireByAffaireLien(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numAffaire") String numAffaire, @PathVariable("idTribunal") long idTribunal) {

		Affaire aData = affaireService.findAffaireByAffaireLien(idEnfant, numAffaire, idTribunal);
		if (aData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  deja lien", aData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Not Found", null);
		}
	}

	@GetMapping("/findByArrestation/{idEnfant}/{numOrdinale}")
	public ApiResponse<List<Affaire>> findByArrestation(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinale") long numOrdinale) {
		 
	 

		List<Affaire> output =  affaireService.findByArrestation(idEnfant, numOrdinale);

		return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "okk", output);

	}

	
	@GetMapping("/calculerAffaire/{idEnfant}/{numOrdinale}")
	public ApiResponse<CalculeAffaireDto> calculerAffaire(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinale") long numOrdinale) {
		 
	 

		CalculeAffaireDto output =  affaireService.calculerAffaire(idEnfant, numOrdinale);

		return new ApiResponse<>(HttpStatus.ACCEPTED.value(), "okk", output);

	}
	
	
	@GetMapping("/findByArrestationToTransfert/{idEnfant}/{numOrdinale}")
	public ApiResponse<List<Affaire>> findByArrestationToTransfert(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinale") long numOrdinale) {

		 
			List<Affaire> output = affaireService.findByArrestationToTransfert(idEnfant, numOrdinale) ;
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "a Not FOund", output);
		 
	}

	@GetMapping("/findByArrestationToArret/{idEnfant}/{numOrdinale}")
	public ApiResponse<List<Affaire>> findByArrestationToArret(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinale") long numOrdinale) {

		 
			List<Affaire> output  = affaireService.findByArrestationToArret(idEnfant, numOrdinale);
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "a Not FOund", output);
		
	}

	@GetMapping("/findByArrestationByCJorCR/{idEnfant}/{numOrdinale}")
	public ApiResponse<List<Affaire>> findByArrestationByCJorCR(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinale") long numOrdinale) {

	 
			List<Affaire> output = affaireService.findByArrestationByCJorCR(idEnfant, numOrdinale);
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "a Not FOund", output);
		 
	}

	@GetMapping("/findByArrestationToPropaga/{idEnfant}/{numOrdinale}")
	public ApiResponse<List<Affaire>> findByArrestationByCDorCHorCP(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinale") long numOrdinale) {

		 
			List<Affaire> output = affaireService.findByArrestationByCDorCHorCP(idEnfant, numOrdinale);

			 
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "a Not FOund", output);
		 
	}

	@GetMapping("/findByNumOrdinalAffaire/{idEnfant}/{numOrdinale}/{numOrdinalAffaire}")
	public ApiResponse<List<Affaire>> findByNumOrdinalAffaire(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinale") long numOrdinale, @PathVariable("numOrdinalAffaire") long numOrdinalAffaire) {

		List<Affaire> aData = affaireService.findByNumOrdinalAffaire(idEnfant, numOrdinale, numOrdinalAffaire);
		if (aData.isEmpty()) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", null);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "a Not FOund", aData);
		}
	}

	@PostMapping("/verifierNumOrdinalAffaire/{numOrdinaleArrestationActuelle}")
	public ApiResponse<Affaire> verifierNumOrdinalAffaire(@RequestBody Affaire affaire,
			@PathVariable("numOrdinaleArrestationActuelle") long numOrdinaleArrestationActuelle) {

	 
		try {
			affaire=affaireService.verifierNumOrdinalAffaire(affaire, numOrdinaleArrestationActuelle);

				return new ApiResponse<>(HttpStatus.OK.value(), " Affaire saved Successfully", affaire);

			 

		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}
	}

	@PutMapping("/update")
	public ApiResponse<Affaire> update(@RequestBody Affaire affaire) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.", affaireService.update(affaire));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  not Saved", null);
		}

	}

	@GetMapping("/getDateDebutPunition/{idEnfant}/{numOrdinale}")
	public ApiResponse<Object> getDateDebutPunition(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinale") long numOrdinale) {

		Date aData = (Date) affaireService.getDateDebutPunition(idEnfant, numOrdinale);
		if (aData != null) {
			return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", aData);
		} else {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "a Not FOund", null);
		}
	}

	@GetMapping("/getDateFinPunition/{idEnfant}/{numOrdinale}")
	public ApiResponse<Object> getDateFinPunition(@PathVariable("idEnfant") String idEnfant,
			@PathVariable("numOrdinale") long numOrdinale) {

		Date aData = (Date) affaireService.getDateFinPunition(idEnfant, numOrdinale);
		return new ApiResponse<>(HttpStatus.OK.value(), "  fetched suucessfully", aData);
				 
		  
	}

 
}
