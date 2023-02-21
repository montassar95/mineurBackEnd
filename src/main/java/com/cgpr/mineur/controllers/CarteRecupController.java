package com.cgpr.mineur.controllers;

import java.util.List;

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

import com.cgpr.mineur.models.AccusationCarteRecup;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.Arrestation;
import com.cgpr.mineur.models.ArretProvisoire;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.models.DocumentId;
import com.cgpr.mineur.repository.AccusationCarteRecupRepository;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.ArretProvisoireRepository;
import com.cgpr.mineur.repository.CarteRecupRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.repository.TypeAffaireRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/carteRecup")
public class CarteRecupController {

	@Autowired
	private CarteRecupRepository carteRecupRepository;

	@Autowired
	private AffaireRepository affaireRepository;

	@Autowired
	private ArrestationRepository arrestationRepository;
 
	
	@Autowired
	private  DocumentRepository documentRepository;
	
	
	@Autowired
	private  AccusationCarteRecupRepository accusationCarteRecupRepository;
	

	
	@Autowired
	private  ArretProvisoireRepository arretProvisoireRepository;
	
	@GetMapping("/all")
	public ApiResponse<List<CarteRecup>> listEtablissement() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.", carteRecupRepository.findAll());
	}

 

//	@GetMapping("/countCarteRecup/{idEnfant}/{numOrdinale}")
//	public ApiResponse<Object> countCarteRecup(@PathVariable("idEnfant") String idEnfant,
//			@PathVariable("numOrdinale") long numOrdinale) {
//
//		System.out.println(idEnfant);
//		System.out.println(numOrdinale);
//		System.out.println(carteRecupRepository.countCarteRecup(idEnfant));
//		return new ApiResponse<>(HttpStatus.OK.value(), "ok", carteRecupRepository.countCarteRecup(idEnfant));
//
//	}

	@PostMapping("/add")
	public ApiResponse<CarteRecup> save(@RequestBody CarteRecup carteRecup) {

		System.out.println(carteRecup.toString());

		System.out.println(carteRecup.getDocumentId().toString());

		if (carteRecup.getAffaire().getAffaireLien() != null) {
			carteRecup.getAffaire().getAffaireLien().setStatut(1);
			System.out.println("=========================debut lien ==================================");
			System.out.println(carteRecup.getAffaire().getAffaireLien().toString());
			carteRecup.getAffaire().setNumOrdinalAffaireByAffaire(
			carteRecup.getAffaire().getAffaireLien().getNumOrdinalAffaireByAffaire() + 1);
 			carteRecup.getAffaire().setTypeDocument("CJ");
 			if(carteRecup.getTypeJuge().getSituation().toString().equals("arret".toString())) {
 				carteRecup.getAffaire().setTypeDocument("CJA");
 			}
//			carteRecup.getAffaire().setDaysDiffJuge(carteRecup.getDaysDiffJuge());
			affaireRepository.save(carteRecup.getAffaire().getAffaireLien());
			System.out.println("============================fin lien===============================");
		}
		System.out.println("================================debut affaire ===========================");
		System.out.println(carteRecup.toString()+ "**********************");
		carteRecup.getAffaire().setTypeAffaire(carteRecup.getTypeAffaire());
		carteRecup.getAffaire().setTypeDocument("CJ");
		if(carteRecup.getTypeJuge().getSituation().toString().equals("arret".toString())) {
				carteRecup.getAffaire().setTypeDocument("CJA");
			}
//		|| carteRecup.getTypeJuge().getSituation().toString().equals("nonCal")
	 	if(carteRecup.getAffaire().getAffaireAffecter() == null || carteRecup.getTypeJuge().getSituation().toString().equals("nonCal")) {
	 		System.out.println("YESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
	 	carteRecup.getAffaire().setDaysDiffJuge(carteRecup.getDaysDiffJuge());
	 	carteRecup.getAffaire().setDateDebutPunition(carteRecup.getDateDebutPunition());
		carteRecup.getAffaire().setDateFinPunition(carteRecup.getDateFinPunition());
	 	}
	 	else {
	 		carteRecup.getAffaire().setDaysDiffJuge(0);
	 	}
	 	
	 	System.out.println(carteRecup.getAffaire());
		 affaireRepository.save(carteRecup.getAffaire());
		System.out.println("==================================fin affaire=========================");
		 List<AccusationCarteRecup> listacc =accusationCarteRecupRepository.findByCarteRecup( carteRecup.getDocumentId()  );
		 List<ArretProvisoire> listarr =arretProvisoireRepository.findArretProvisoireByCarteRecup( carteRecup.getDocumentId()  );
		 
		 if(!listacc.isEmpty()) {
			 
			 for(AccusationCarteRecup acc :  listacc){
				 accusationCarteRecupRepository.delete(acc);
			 }
			
		 }
		 
		 
         if(!listarr.isEmpty()) {
			 
			 for(ArretProvisoire arr :  listarr){
				 arretProvisoireRepository.delete(arr);
			 }
			
		 }
		 
		 
		CarteRecup c =carteRecupRepository.save(carteRecup);
//		Arrestation ar = arrestationRepository.findByIdEnfantAndStatut0(c.getDocumentId().getIdEnfant());
//		 
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
		try {
			return new ApiResponse<>(HttpStatus.OK.value(), "  saved Successfully", c);
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), "  not saved", null);
		}

	}

	@PutMapping("/update")
	public ApiResponse<CarteRecup> update(@RequestBody CarteRecup carteRecup) {
		try {

			return new ApiResponse<>(HttpStatus.OK.value(), "  updated successfully.",
					carteRecupRepository.save(carteRecup));
		} catch (Exception e) {
			return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "  not Saved", null);
		}

	}
 
}
