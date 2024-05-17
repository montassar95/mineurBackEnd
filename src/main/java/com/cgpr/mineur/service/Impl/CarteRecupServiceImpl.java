package com.cgpr.mineur.service.Impl;


 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.AccusationCarteRecup;
import com.cgpr.mineur.models.ArretProvisoire;
import com.cgpr.mineur.models.CarteRecup;
import com.cgpr.mineur.repository.AccusationCarteRecupRepository;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.ArretProvisoireRepository;
import com.cgpr.mineur.repository.CarteRecupRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.service.CarteRecupService;
 
 
 
@Service
public class CarteRecupServiceImpl implements CarteRecupService  {

	
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
	
	@Override
	public  List<CarteRecup>   listEtablissement() {
		return   (List<CarteRecup>) carteRecupRepository.findAll() ;
	}

 

 

	@Override
	public  CarteRecup  save(  CarteRecup carteRecup) {

		System.out.println(carteRecup.toString());

		System.out.println(carteRecup.getDocumentId().toString());

		if (carteRecup.getAffaire().getAffaireLien() != null) {
			carteRecup.getAffaire().getAffaireLien().setStatut(1);
			System.out.println("=========================debut lien ==================================");
			System.out.println(carteRecup.getAffaire().getAffaireLien().toString());
			carteRecup.getAffaire().setNumOrdinalAffaireByAffaire(
			carteRecup.getAffaire().getAffaireLien().getNumOrdinalAffaireByAffaire() + 1);
 			carteRecup.getAffaire().setTypeDocument("CJ");
 			carteRecup.getAffaire().setTypeJuge(carteRecup.getTypeJuge()); 
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
		carteRecup.getAffaire().setTypeJuge(carteRecup.getTypeJuge()); 
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
 
		try {
			return   c ;
		} catch (Exception e) {
			return   null ;
		}

	}

	@Override
	public  CarteRecup  update(  CarteRecup carteRecup) {
		try {

			return  carteRecupRepository.save(carteRecup) ;
		} catch (Exception e) {
			return   null ;
		}

	}
	 
	 
	
}