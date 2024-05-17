package com.cgpr.mineur.service.Impl;


 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.AccusationCarteDepot;
import com.cgpr.mineur.models.CarteDepot;
import com.cgpr.mineur.repository.AccusationCarteDepotRepository;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.CarteDepotRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.service.CarteDepotService;
 
 
 
@Service
public class CarteDepotServiceImpl implements CarteDepotService {

	
	@Autowired
	private CarteDepotRepository carteDepotRepository;

	@Autowired
	private AffaireRepository affaireRepository;

	@Autowired
	private ArrestationRepository arrestationRepository;
 
	
	@Autowired
	private  DocumentRepository documentRepository;
	
	
	@Autowired
	private  AccusationCarteDepotRepository accusationCarteDepotRepository;
	
 
	
	
	
	
	
	
	
	@Override
	public  CarteDepot  save( CarteDepot carteDepot) {

		 
		if (carteDepot.getAffaire().getAffaireLien() != null) {
			carteDepot.getAffaire().getAffaireLien().setStatut(1);
			System.out.println("=========================debut lien ==================================");
			System.out.println(carteDepot.getAffaire().getAffaireLien().toString());
			carteDepot.getAffaire().setNumOrdinalAffaireByAffaire(
			carteDepot.getAffaire().getAffaireLien().getNumOrdinalAffaireByAffaire() + 1);
			carteDepot.getAffaire().setTypeDocument("CD");
			affaireRepository.save(carteDepot.getAffaire().getAffaireLien());
			System.out.println("============================fin lien===============================");
		}
		System.out.println("================================debut affaire ===========================");
		System.out.println(carteDepot.getAffaire().toString());
		carteDepot.getAffaire().setTypeDocument("CD");
		carteDepot.getAffaire().setTypeAffaire(carteDepot.getTypeAffaire());
		affaireRepository.save(carteDepot.getAffaire());
		System.out.println("==================================fin affaire=========================");
		
		
		 List<AccusationCarteDepot> listacc =accusationCarteDepotRepository.findByCarteDepot( carteDepot.getDocumentId()  );
		
		 
		 if(!listacc.isEmpty()) {
			 
			 for(AccusationCarteDepot acc :  listacc){
				 accusationCarteDepotRepository.delete(acc);
			 }
			
		 }
		CarteDepot c = carteDepotRepository.save(carteDepot);
		 
		

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
// 		ar.setNumOrdinalAffairePricipale(affprincipale.get(0).getNumOrdinalAffaire());
//		arrestationRepository.save(ar);
		//Etat.etatJuridique 

		try {
			return  c ;
		} catch (Exception e) {
			return   null ;
		}

	}
}