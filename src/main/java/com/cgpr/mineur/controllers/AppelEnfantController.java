package com.cgpr.mineur.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.AppelEnfant;
import com.cgpr.mineur.models.Arrestation;
import com.cgpr.mineur.models.Transfert;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.AppelEnfantRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.repository.TransfertRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/appelEnfant")
public class AppelEnfantController {

	@Autowired
	private AppelEnfantRepository appelEnfantRepository;

	@Autowired
	private AffaireRepository affaireRepository;

	@Autowired
	private ArrestationRepository arrestationRepository;

	@Autowired
	private DocumentRepository documentRepository;

	@PostMapping("/add")
	public ApiResponse<AppelEnfant> save(@RequestBody AppelEnfant appelEnfant) {

		System.out.println(appelEnfant.toString());

		System.out.println(appelEnfant.getDocumentId().toString());

		System.out.println("================================debut affaire ===========================");
		System.out.println(appelEnfant.getAffaire().toString());
		appelEnfant.getAffaire().setTypeDocument("AE");
		appelEnfant.getAffaire().setTypeAffaire(appelEnfant.getAffaire().getTypeAffaire());
		affaireRepository.save(appelEnfant.getAffaire());
		System.out.println("==================================fin affaire=========================");
		appelEnfant.getAffaire().setNumOrdinalAffaireByAffaire(3);
		appelEnfant.setTypeAffaire(appelEnfant.getAffaire().getTypeAffaire());
		AppelEnfant c = appelEnfantRepository.save(appelEnfant);

//		Arrestation ar = arrestationRepository.findByIdEnfantAndStatut0(c.getDocumentId().getIdEnfant());
//		List<Affaire> aData = documentRepository.findStatutJurByArrestation(c.getDocumentId().getIdEnfant() );
//		 
//		if (aData.isEmpty()) {
//			ar.setEtatJuridique ("juge");
//		} else {
//			ar.setEtatJuridique( "arret");
//		}
//		List<Affaire> affprincipale = affaireRepository.findAffairePrincipale(c.getDocumentId().getIdEnfant(),c.getDocumentId().getNumOrdinalArrestation(),PageRequest.of(0,1));
// 		ar.setNumAffairePricipale(affprincipale.get(0).getAffaireId().getNumAffaire()); 
// 		ar.setTribunalPricipale(affprincipale.get(0).getTribunal());
//	    ar.setNumOrdinalAffairePricipale(affprincipale.get(0).getNumOrdinalAffaire());
//		arrestationRepository.save(ar);
		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}

	}

}
