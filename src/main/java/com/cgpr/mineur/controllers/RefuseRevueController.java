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
import com.cgpr.mineur.models.Arreterlexecution;
import com.cgpr.mineur.models.RefuseRevue;
import com.cgpr.mineur.models.Revue;
import com.cgpr.mineur.models.Transfert;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.AppelEnfantRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.repository.RefuseRevueRepository;
import com.cgpr.mineur.repository.RevueRepository;
import com.cgpr.mineur.repository.TransfertRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/refuseRevue")
public class RefuseRevueController {

	
	@Autowired
	private RefuseRevueRepository refuseRevueRepository;

	@Autowired
	private AffaireRepository affaireRepository;

	 
 
	@Autowired
	private ArrestationRepository arrestationRepository;
 
	
	@Autowired
	private  DocumentRepository documentRepository;
 

 
	@PostMapping("/add")
	public ApiResponse<RefuseRevue> save(@RequestBody RefuseRevue refuseRevue) {

		if (refuseRevue.getAffaire().getAffaireLien() != null) {
			refuseRevue.getAffaire().getAffaireLien().setStatut(1);
			System.out.println("=========================debut lien ==================================");
		 
			refuseRevue.getAffaire().setNumOrdinalAffaireByAffaire(
					refuseRevue.getAffaire().getAffaireLien().getNumOrdinalAffaireByAffaire() + 1);
			
			refuseRevue.getAffaire().setTypeDocument("CRR");
			refuseRevue.getAffaire().setTypeAffaire(refuseRevue.getAffaire().getAffaireLien().getTypeAffaire());
			
			
             // refuseRevue.getAffaire().setDaysDiffJuge(refuseRevue.getAffaire().getDaysDiffJuge());
             // refuseRevue.getAffaire().setDateDebutPunition(refuseRevue.getAffaire().getDateDebutPunition());
			 // because i find date fin puntion where statut affair equalse 0
			refuseRevue.getAffaire().setDateDebutPunition(refuseRevue.getAffaire().getAffaireLien().getDateDebutPunition());
			refuseRevue.getAffaire().setDateFinPunition(refuseRevue.getAffaire().getAffaireLien().getDateFinPunition());
			
			
			affaireRepository.save(refuseRevue.getAffaire().getAffaireLien());
			System.out.println("============================fin lien===============================");
		}
		System.out.println("================================debut affaire ===========================");
		System.out.println(refuseRevue.getAffaire().toString());
		refuseRevue.getAffaire().setTypeDocument("CRR");
		

		
		
		affaireRepository.save(refuseRevue.getAffaire());
		System.out.println("==================================fin affaire=========================");
		refuseRevue.setTypeAffaire(refuseRevue.getAffaire().getTypeAffaire());
		RefuseRevue c = refuseRevueRepository.save(refuseRevue);
 
 
		
		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully", c);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}

	}
 
 
}
