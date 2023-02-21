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
import com.cgpr.mineur.models.Revue;
import com.cgpr.mineur.models.Transfert;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.AppelEnfantRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.repository.RevueRepository;
import com.cgpr.mineur.repository.TransfertRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/revue")
public class RevueController {

	
	@Autowired
	private RevueRepository revueRepository;

	@Autowired
	private AffaireRepository affaireRepository;

	 
 
	@Autowired
	private ArrestationRepository arrestationRepository;
 
	
	@Autowired
	private  DocumentRepository documentRepository;
 

 
	@PostMapping("/add")
	public ApiResponse<Revue> save(@RequestBody Revue revue) {

		if (revue.getAffaire().getAffaireLien() != null) {
			revue.getAffaire().getAffaireLien().setStatut(1);
			System.out.println("=========================debut lien ==================================");
		 
			revue.getAffaire().setNumOrdinalAffaireByAffaire(
					revue.getAffaire().getAffaireLien().getNumOrdinalAffaireByAffaire() + 1);
			
			revue.getAffaire().setTypeDocument("CR");
			revue.getAffaire().setTypeAffaire(revue.getAffaire().getAffaireLien().getTypeAffaire());
			affaireRepository.save(revue.getAffaire().getAffaireLien());
			System.out.println("============================fin lien===============================");
		}
		System.out.println("================================debut affaire ===========================");
		System.out.println(revue.getAffaire().toString());
		revue.getAffaire().setTypeDocument("CR");
		affaireRepository.save(revue.getAffaire());
		System.out.println("==================================fin affaire=========================");
		revue.setTypeAffaire(revue.getAffaire().getTypeAffaire());
		Revue c = revueRepository.save(revue);
 
//		System.out.println("================================debut affaire ===========================");
//		System.out.println(revue.getAffaire().toString());
//		revue.getAffaire().setTypeDocument("CR");
//		revue.getAffaire().setTypeAffaire(revue.getAffaire().getTypeAffaire());
//		affaireRepository.save(revue.getAffaire());
//		System.out.println("==================================fin affaire=========================");
//		revue.getAffaire().setNumOrdinalAffaireByAffaire(3) ;
//		revue.setTypeAffaire(revue.getAffaire().getTypeAffaire());
//		Revue c =	revueRepository.save(revue);

		
//		Arrestation ar = arrestationRepository.findByIdEnfantAndStatut0(c.getDocumentId().getIdEnfant());
//		List<Affaire> aData = documentRepository.findStatutJurByArrestation(c.getDocumentId().getIdEnfant() );
//		 
//		if (aData.isEmpty()) {
//			ar.setEtatJuridique ("juge");
//		} else {
//			ar.setEtatJuridique( "arret");
//		}
//		 
//		List<Affaire> affprincipale = affaireRepository.findAffairePrincipale(c.getDocumentId().getIdEnfant(),c.getDocumentId().getNumOrdinalArrestation(),PageRequest.of(0,1));
// 		ar.setNumAffairePricipale(affprincipale.get(0).getAffaireId().getNumAffaire()); 
// 		ar.setTribunalPricipale(affprincipale.get(0).getTribunal());
// 		  ar.setNumOrdinalAffairePricipale(affprincipale.get(0).getNumOrdinalAffaire());
//		arrestationRepository.save(ar);
		
//		
//		List<Affaire> aData = documentRepository.findByArrestation(c.getDocumentId().getIdEnfant() );
//		Affaire a = aData.stream()
//		            .peek(num -> System.out.println("will filter " + num.getTypeDocument()))
//								              .filter(x -> x.getTypeDocument().equals("CD")  || 
//									            		   x.getTypeDocument().equals("CH")   ||
//									              		   x.getTypeDocument().equals("T")   || 
//									              		   x.getTypeDocument().equals("AP")   ||
//									              	  	   x.getTypeDocument().equals("AE")   
//								              	  )
//		              .findFirst()
//		              .orElse(null);
//		
		
		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully", c);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}

	}
 
 
}
