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
import com.cgpr.mineur.models.ChangementLieu;
import com.cgpr.mineur.models.Transfert;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.ArreterlexecutionRepository;
import com.cgpr.mineur.repository.ChangementLieuRepository;
import com.cgpr.mineur.repository.DocumentRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/changementLieu")
public class ChangementLieuController {

	
	@Autowired
	private ChangementLieuRepository changementLieuRepository;

	@Autowired
	private AffaireRepository affaireRepository;

	 
 
	@Autowired
	private ArrestationRepository arrestationRepository;
 
	
	@Autowired
	private  DocumentRepository documentRepository;

 
	@PostMapping("/add")
	public ApiResponse<ChangementLieu> save(@RequestBody ChangementLieu changementLieu) {

		 
	 

		if (changementLieu.getAffaire().getAffaireLien() != null) {
			changementLieu.getAffaire().getAffaireLien().setStatut(1);
			System.out.println("=========================debut lien ==================================");
		 
			changementLieu.getAffaire().setNumOrdinalAffaireByAffaire(
					changementLieu.getAffaire().getAffaireLien().getNumOrdinalAffaireByAffaire() + 1);
			
		 	changementLieu.getAffaire().setTypeDocument(changementLieu.getAffaire().getAffaireLien().getTypeDocument().toString());
			changementLieu.getAffaire().setTypeAffaire(changementLieu.getAffaire().getAffaireLien().getTypeAffaire());
			affaireRepository.save(changementLieu.getAffaire().getAffaireLien());
			System.out.println("============================fin lien===============================");
		}
		System.out.println("================================debut affaire ===========================");
		System.out.println(changementLieu.getAffaire().toString());
		//changementLieu.getAffaire().setTypeDocument("CHL");
		//affaireRepository.save(changementLieu.getAffaire());
		System.out.println("==================================fin affaire=========================");
	 changementLieu.setTypeAffaire(changementLieu.getAffaire().getTypeAffaire());
		affaireRepository.save(changementLieu.getAffaire());
		System.out.println(changementLieu.toString());
		ChangementLieu c = changementLieuRepository.save(changementLieu);
		
 
//		arrestationRepository.save(ar);

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully", null);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}

	}
 
 
}
