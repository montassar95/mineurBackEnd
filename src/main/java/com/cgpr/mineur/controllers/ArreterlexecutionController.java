package com.cgpr.mineur.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.Arreterlexecution;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.ArreterlexecutionRepository;
import com.cgpr.mineur.repository.DocumentRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/arreterlexecution")
public class ArreterlexecutionController {

	@Autowired
	private ArreterlexecutionRepository arreterlexecutionRepository;

	@Autowired
	private AffaireRepository affaireRepository;

	@Autowired
	private ArrestationRepository arrestationRepository;

	@Autowired
	private DocumentRepository documentRepository;

	@PostMapping("/add")
	public ApiResponse<Arreterlexecution> save(@RequestBody Arreterlexecution arreterlexecution) {

		if (arreterlexecution.getAffaire().getAffaireLien() != null) {
			arreterlexecution.getAffaire().getAffaireLien().setStatut(1);
			System.out.println("=========================debut lien ==================================");

			arreterlexecution.getAffaire().setNumOrdinalAffaireByAffaire(
					arreterlexecution.getAffaire().getAffaireLien().getNumOrdinalAffaireByAffaire() + 1);

			arreterlexecution.getAffaire().setTypeDocument("AEX");
			arreterlexecution.getAffaire()
					.setTypeAffaire(arreterlexecution.getAffaire().getAffaireLien().getTypeAffaire());
			affaireRepository.save(arreterlexecution.getAffaire().getAffaireLien());
			System.out.println("============================fin lien===============================");
		}
		System.out.println("================================debut affaire ===========================");
		System.out.println(arreterlexecution.getAffaire().toString());
		arreterlexecution.getAffaire().setTypeDocument("AEX");
		affaireRepository.save(arreterlexecution.getAffaire());
		System.out.println("==================================fin affaire=========================");
		arreterlexecution.setTypeAffaire(arreterlexecution.getAffaire().getTypeAffaire());
		Arreterlexecution c = arreterlexecutionRepository.save(arreterlexecution);

//		arrestationRepository.save(ar);

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}

	}

}
