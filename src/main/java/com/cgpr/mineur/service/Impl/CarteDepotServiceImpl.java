package com.cgpr.mineur.service.Impl;


 
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.AccusationCarteDepotConverter;
import com.cgpr.mineur.converter.AffaireConverter;
import com.cgpr.mineur.converter.CarteDepotConverter;
import com.cgpr.mineur.converter.DocumentConverter;
import com.cgpr.mineur.converter.DocumentIdConverter;
import com.cgpr.mineur.converter.TitreAccusationConverter;
import com.cgpr.mineur.dto.CarteDepotDto;
import com.cgpr.mineur.dto.DocumentDto;
import com.cgpr.mineur.dto.TitreAccusationDto;
import com.cgpr.mineur.models.AccusationCarteDepot;
import com.cgpr.mineur.models.AccusationCarteDepotId;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.CarteDepot;
import com.cgpr.mineur.models.Document;
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
	public  CarteDepotDto  save( CarteDepotDto carteDepotDto) {
		System.err.println("1 = "+carteDepotDto.toString());
		 
		if (carteDepotDto.getAffaire().getAffaireLien() != null) {
			carteDepotDto.getAffaire().getAffaireLien().setStatut(1);
			System.out.println("=========================debut lien ==================================");
			System.out.println(carteDepotDto.getAffaire().getAffaireLien().toString());
			carteDepotDto.getAffaire().setNumOrdinalAffaireByAffaire(
			carteDepotDto.getAffaire().getAffaireLien().getNumOrdinalAffaireByAffaire() + 1);
			carteDepotDto.getAffaire().setTypeDocument("CD");
			
			Affaire affaireSaved = affaireRepository.save(AffaireConverter.dtoToEntity(carteDepotDto.getAffaire().getAffaireLien()));
			affaireRepository.save(affaireSaved);
			System.out.println("============================fin lien===============================");
		}
		System.out.println("================================debut affaire ===========================");
		System.out.println(carteDepotDto.getAffaire().toString());
		carteDepotDto.getAffaire().setTypeDocument("CD");
		carteDepotDto.getAffaire().setTypeAffaire(carteDepotDto.getTypeAffaire());
		System.err.println("2 = "+ AffaireConverter.dtoToEntity(carteDepotDto.getAffaire() ).toString());
		
		Affaire affaireSaved = affaireRepository.save(AffaireConverter.dtoToEntity(carteDepotDto.getAffaire() ));
		affaireRepository.save(affaireSaved);

		System.out.println("==================================fin affaire=========================");
		
		
		 List<AccusationCarteDepot> listacc =accusationCarteDepotRepository.findByCarteDepot(DocumentIdConverter.dtoToEntity( carteDepotDto.getDocumentId()   ) );
		
		 
		 if(!listacc.isEmpty()) {
			 
			 for(AccusationCarteDepot acc :  listacc){
				 accusationCarteDepotRepository.delete(acc);
			 }
			
		 }
		 DocumentDto d =(DocumentDto)carteDepotDto;
		 System.err.println(d.getDocumentId().toString());
		 System.err.println("2 = "+carteDepotDto.toString());
		 System.err.println("3 = "+CarteDepotConverter.dtoToEntity(carteDepotDto).toString());
		CarteDepot carteDepotSaved = carteDepotRepository.save(CarteDepotConverter.dtoToEntity(carteDepotDto));
		 
		System.out.println("salut service "+ carteDepotSaved.toString());

		
		
		
	 
		   List<AccusationCarteDepot> accusationCarteDepotSaved = new ArrayList<AccusationCarteDepot>();
		
		System.out.println(carteDepotDto.getTitreAccusations().size());
		
		for(TitreAccusationDto a: carteDepotDto.getTitreAccusations()) {
			System.out.println("tohmaaaa");
			System.out.println(a.toString());
			 
			AccusationCarteDepotId acdId = new AccusationCarteDepotId() ;
			acdId.setIdEnfant(carteDepotSaved.getDocumentId().getIdEnfant());
			 
			acdId.setNumOrdinalAffaire(carteDepotSaved.getDocumentId().getNumOrdinalAffaire()); 
			acdId.setNumOrdinalArrestation(carteDepotSaved.getDocumentId().getNumOrdinalAffaire());
			acdId.setNumOrdinalDoc(carteDepotSaved.getDocumentId().getNumOrdinalDoc());
			acdId.setNumOrdinalDocByAffaire(carteDepotSaved.getDocumentId().getNumOrdinalDocByAffaire());
			acdId.setIdTitreAccusation(a.getId());
			
			AccusationCarteDepot acd =acd = new AccusationCarteDepot() ;
			acd.setAccusationCarteDepotId(acdId);
			acd.setCarteDepot(carteDepotSaved);
			acd.setTitreAccusation( TitreAccusationConverter.dtoToEntity(a)  );
			AccusationCarteDepot accusationCarteDepot=	accusationCarteDepotRepository.save(acd);
			System.err.println("is"+accusationCarteDepot.toString());
			accusationCarteDepotSaved.add(accusationCarteDepot);
 			
		}
		carteDepotSaved.setAccusationCarteDepots(accusationCarteDepotSaved) ;
 

		try {
			return  CarteDepotConverter.entityToDto(carteDepotSaved);
		} catch (Exception e) {
			return  CarteDepotConverter.entityToDto(carteDepotSaved);
		}

	}
}