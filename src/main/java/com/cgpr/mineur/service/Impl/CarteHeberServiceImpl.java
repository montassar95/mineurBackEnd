package com.cgpr.mineur.service.Impl;


 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.AccusationCarteHeber;
import com.cgpr.mineur.models.CarteHeber;
import com.cgpr.mineur.repository.AccusationCarteHeberRepository;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.CarteHeberRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.service.CarteHeberService;
 
 
 
@Service
public class CarteHeberServiceImpl implements CarteHeberService  {

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
	
	
	@Override
	public  CarteHeber  save( CarteHeber carteHeber) {

		 
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
			return   c ;
		} catch (Exception e) {
			return  null ;
		}

	}
 
	 
	 
}