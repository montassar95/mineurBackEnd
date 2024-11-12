package com.cgpr.mineur.service.Impl;


 
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.AffaireConverter;
import com.cgpr.mineur.converter.CarteHeberConverter;
import com.cgpr.mineur.converter.CarteHeberConverter;
import com.cgpr.mineur.converter.DocumentIdConverter;
import com.cgpr.mineur.converter.TitreAccusationConverter;
import com.cgpr.mineur.dto.CarteHeberDto;
import com.cgpr.mineur.dto.DocumentDto;
import com.cgpr.mineur.dto.TitreAccusationDto;
import com.cgpr.mineur.models.AccusationCarteHeber;
import com.cgpr.mineur.models.AccusationCarteHeberId;
import com.cgpr.mineur.models.AccusationCarteHeber;
import com.cgpr.mineur.models.AccusationCarteHeberId;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.CarteHeber;
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
	public  CarteHeberDto    save( CarteHeberDto   carteHeberDto    ) {
		System.err.println("1 = "+carteHeberDto.toString());
		 
		if (carteHeberDto.getAffaire().getAffaireLien() != null) {
			carteHeberDto.getAffaire().getAffaireLien().setStatut(1);
			System.out.println("=========================debut lien ==================================");
			System.out.println(carteHeberDto.getAffaire().getAffaireLien().toString());
			carteHeberDto.getAffaire().setNumOrdinalAffaireByAffaire(
			carteHeberDto.getAffaire().getAffaireLien().getNumOrdinalAffaireByAffaire() + 1);
			carteHeberDto.getAffaire().setTypeDocument("CH");
			
			Affaire affaireSaved = affaireRepository.save(AffaireConverter.dtoToEntity(carteHeberDto.getAffaire().getAffaireLien()));
			affaireRepository.save(affaireSaved);
			System.out.println("============================fin lien===============================");
		}
		System.out.println("================================debut affaire ===========================");
		System.out.println(carteHeberDto.getAffaire().toString());
		carteHeberDto.getAffaire().setTypeDocument("CH");
		carteHeberDto.getAffaire().setTypeAffaire(carteHeberDto.getTypeAffaire());
		Affaire affaireSaved = affaireRepository.save(AffaireConverter.dtoToEntity(carteHeberDto.getAffaire() ));
		affaireRepository.save(affaireSaved);

		System.out.println("==================================fin affaire=========================");
		
		
		 List<AccusationCarteHeber> listacc =accusationCarteHeberRepository.findByCarteHeber(DocumentIdConverter.dtoToEntity( carteHeberDto.getDocumentId()   ) );
		
		 
		 if(!listacc.isEmpty()) {
			 
			 for(AccusationCarteHeber acc :  listacc){
				 accusationCarteHeberRepository.delete(acc);
			 }
			
		 }
		 DocumentDto d =(DocumentDto)carteHeberDto;
		 System.err.println(d.getDocumentId().toString());
		 System.err.println("2 = "+carteHeberDto.toString());
		 System.err.println("3 = "+CarteHeberConverter.dtoToEntity(carteHeberDto).toString());
		CarteHeber carteHeberSaved = carteHeberRepository.save(CarteHeberConverter.dtoToEntity(carteHeberDto));
		 
		System.out.println("salut service "+ carteHeberSaved.toString());

		
		
		
	 
		   List<AccusationCarteHeber> accusationCarteHeberSaved = new ArrayList<AccusationCarteHeber>();
		
		System.out.println(carteHeberDto.getTitreAccusations().size());
		
		for(TitreAccusationDto a: carteHeberDto.getTitreAccusations()) {
			System.out.println("tohmaaaa");
			System.out.println(a.toString());
			 
			AccusationCarteHeberId acdId = new AccusationCarteHeberId() ;
			acdId.setIdEnfant(carteHeberSaved.getDocumentId().getIdEnfant());
			 
			acdId.setNumOrdinalAffaire(carteHeberSaved.getDocumentId().getNumOrdinalAffaire()); 
			acdId.setNumOrdinalArrestation(carteHeberSaved.getDocumentId().getNumOrdinalAffaire());
			acdId.setNumOrdinalDoc(carteHeberSaved.getDocumentId().getNumOrdinalDoc());
			acdId.setNumOrdinalDocByAffaire(carteHeberSaved.getDocumentId().getNumOrdinalDocByAffaire());
			acdId.setIdTitreAccusation(a.getId());
			
			AccusationCarteHeber acd =acd = new AccusationCarteHeber() ;
			acd.setAccusationCarteHeberId(acdId);
			acd.setCarteHeber(carteHeberSaved);
			acd.setTitreAccusation( TitreAccusationConverter.dtoToEntity(a)  );
			AccusationCarteHeber accusationCarteHeber=	accusationCarteHeberRepository.save(acd);
			System.err.println("is"+accusationCarteHeber.toString());
			accusationCarteHeberSaved.add(accusationCarteHeber);
			
		}
		carteHeberSaved.setAccusationCarteHebers(accusationCarteHeberSaved) ;


		try {
			return  CarteHeberConverter.entityToDto(carteHeberSaved);
		} catch (Exception e) {
			return  CarteHeberConverter.entityToDto(carteHeberSaved);
		}

	}
}
	 
	 
 