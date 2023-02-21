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
import com.cgpr.mineur.models.Arrestation;
import com.cgpr.mineur.models.Transfert;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.repository.TransfertRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/transfert")
public class TransfertController {

	
	@Autowired
	private TransfertRepository transfertRepository;

	@Autowired
	private AffaireRepository affaireRepository;

	 
 
	@Autowired
	private ArrestationRepository arrestationRepository;
 
	
	@Autowired
	private  DocumentRepository documentRepository;

 
	@PostMapping("/add")
	public ApiResponse<Transfert> save(@RequestBody Transfert transfert) {

		System.out.println(transfert.toString());

		System.out.println(transfert.getDocumentId().toString());

		if (transfert.getAffaire().getAffaireLien() != null) {
			transfert.getAffaire().getAffaireLien().setStatut(2);
			System.out.println("=========================debut lien ==================================");
			System.out.println(transfert.getAffaire().getAffaireLien().toString());
			transfert.getAffaire().setNumOrdinalAffaireByAffaire(
					transfert.getAffaire().getAffaireLien().getNumOrdinalAffaireByAffaire() + 1);
			
			transfert.getAffaire().setTypeDocument("T");
			transfert.getAffaire().setTypeAffaire(transfert.getAffaire().getAffaireLien().getTypeAffaire());
			affaireRepository.save(transfert.getAffaire().getAffaireLien());
			System.out.println("============================fin lien===============================");
		}
		System.out.println("================================debut affaire ===========================");
		System.out.println(transfert.getAffaire().toString());
		transfert.getAffaire().setTypeDocument("T");
		affaireRepository.save(transfert.getAffaire());
		System.out.println("==================================fin affaire=========================");
		
		transfert.setTypeAffaire(transfert.getAffaire().getTypeAffaire());
		
		System.out.println(transfert);
		Transfert c = transfertRepository.save(transfert);
		
 
		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}

	}
 
 
}
