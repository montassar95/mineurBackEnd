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

import com.cgpr.mineur.models.AccusationCarteDepot;
import com.cgpr.mineur.models.AccusationCarteHeber;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.Arrestation;
import com.cgpr.mineur.models.CarteDepot;
import com.cgpr.mineur.models.CarteHeber;
import com.cgpr.mineur.models.Transfert;
import com.cgpr.mineur.repository.AccusationCarteDepotRepository;
import com.cgpr.mineur.repository.AccusationCarteHeberRepository;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.CarteHeberRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.repository.TransfertRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/carteHeber")
public class CarteHeberController {

	@Autowired
	private CarteHeberRepository carteHeberRepository;

	@Autowired
	private AffaireRepository affaireRepository;

	@Autowired
	private ArrestationRepository arrestationRepository;
 
	
	@Autowired
	private  DocumentRepository documentRepository;
 
	
	@Autowired
	private  AccusationCarteHeberRepository accusationCarteHeberRepository;
	
	
	@PostMapping("/add")
	public ApiResponse<CarteHeber> save(@RequestBody CarteHeber carteHeber) {

		 
		if (carteHeber.getAffaire().getAffaireLien() != null) {
			carteHeber.getAffaire().getAffaireLien().setStatut(1);
			System.out.println("=========================debut lien ==================================");
			System.out.println(carteHeber.getAffaire().getAffaireLien().toString());
			carteHeber.getAffaire().setNumOrdinalAffaireByAffaire(
			carteHeber.getAffaire().getAffaireLien().getNumOrdinalAffaireByAffaire() + 1);
			carteHeber.getAffaire().setTypeDocument("CH");
			affaireRepository.save(carteHeber.getAffaire().getAffaireLien());
			System.out.println("============================fin lien===============================");
		}
		System.out.println("================================debut affaire ===========================");
		System.out.println(carteHeber.getAffaire().toString());
		carteHeber.getAffaire().setTypeDocument("CH");
		carteHeber.getAffaire().setTypeAffaire(carteHeber.getTypeAffaire());
		affaireRepository.save(carteHeber.getAffaire());
		System.out.println("==================================fin affaire=========================");
		
		
		 List<AccusationCarteHeber> listacc =accusationCarteHeberRepository.findByCarteHeber( carteHeber.getDocumentId()  );
		
		 
		 if(!listacc.isEmpty()) {
			 
			 for(AccusationCarteHeber acc :  listacc){
				 accusationCarteHeberRepository.delete(acc);
			 }
			
		 }
		CarteHeber c = carteHeberRepository.save(carteHeber);
		 
		

		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully", c);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}

	}
 
 
}
