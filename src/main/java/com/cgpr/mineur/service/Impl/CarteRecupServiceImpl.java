package com.cgpr.mineur.service.Impl;


 
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.persistence.Entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.AccusationCarteRecupConverter;
import com.cgpr.mineur.converter.AffaireConverter;
import com.cgpr.mineur.converter.ArretProvisoireConverter;
import com.cgpr.mineur.converter.CarteRecupConverter;
import com.cgpr.mineur.converter.DocumentIdConverter;
import com.cgpr.mineur.dto.CarteRecupDto;
import com.cgpr.mineur.models.AccusationCarteRecup;
import com.cgpr.mineur.models.AccusationCarteRecupId;
import com.cgpr.mineur.models.ArretProvisoire;
import com.cgpr.mineur.models.ArretProvisoireId;
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
	public  List<CarteRecupDto>   listEtablissement() {
		
		List<CarteRecup> list = (List<CarteRecup>) carteRecupRepository.findAll(); 
		  
		return  list .stream().map(CarteRecupConverter::entityToDto).collect(Collectors.toList()) ; 
	}

 

 

	@Override
	public  CarteRecupDto  save(  CarteRecupDto carteRecupDto) {
	
	 

		
		//si NumOrdinalDocByAffaire != 0 cad il s'agit de  update
		if(carteRecupDto.getDocumentId().getNumOrdinalDocByAffaire()==0) {
			long numOrdinalDocByAffaire =  documentRepository.countDocumentByAffaire(carteRecupDto.getDocumentId().getIdEnfant(),
																						carteRecupDto.getDocumentId().getNumOrdinalArrestation(),
																						carteRecupDto.getDocumentId().getNumOrdinalAffaire());
			carteRecupDto.getDocumentId().setNumOrdinalDocByAffaire(numOrdinalDocByAffaire+1);
		}
			
			
		if (carteRecupDto.getAffaire().getAffaireLien() != null) {
			carteRecupDto.getAffaire().getAffaireLien().setStatut(1);
		 
			carteRecupDto.getAffaire().setNumOrdinalAffaireByAffaire(
					carteRecupDto.getAffaire().getAffaireLien().getNumOrdinalAffaireByAffaire() + 1);
			 
 
			affaireRepository.save( AffaireConverter.dtoToEntity(carteRecupDto.getAffaire().getAffaireLien()));
			 
		}
		 
	 
		carteRecupDto.getAffaire().setTypeAffaire(carteRecupDto.getTypeAffaire());
		
		
		
		carteRecupDto.getAffaire().setTypeJuge(carteRecupDto.getTypeJuge()); 
		
		
		if(carteRecupDto.getTypeJuge().getSituation().toString().equals("arret".toString())) {
			carteRecupDto.getAffaire().setTypeDocument("CJA");
			carteRecupDto.setTypeDocument("CJA");
			}
		else {
			carteRecupDto.getAffaire().setTypeDocument("CJ");
			carteRecupDto.setTypeDocument("CJ");
		}
 
	 	if(carteRecupDto.getAffaire().getAffaireAffecter() == null || carteRecupDto.getTypeJuge().getSituation().toString().equals("nonCal")) {
	 		 
	 		carteRecupDto.getAffaire().setDaysDiffJuge(carteRecupDto.getDaysDiffJuge());
	 		carteRecupDto.getAffaire().setDateDebutPunition(carteRecupDto.getDateDebutPunition());
	 		carteRecupDto.getAffaire().setDateFinPunition(carteRecupDto.getDateFinPunition());
	 	}
	 	else {
	 		carteRecupDto.getAffaire().setDaysDiffJuge(0);
	 	}
	 	
	  
		 affaireRepository.save( AffaireConverter.dtoToEntity(carteRecupDto.getAffaire()));
	 
		
		List<AccusationCarteRecup> listacc =accusationCarteRecupRepository.findByCarteRecup( DocumentIdConverter.dtoToEntity(carteRecupDto.getDocumentId())   );
	
		 List<ArretProvisoire> listarr =arretProvisoireRepository.findArretProvisoireByCarteRecup(  DocumentIdConverter.dtoToEntity(carteRecupDto.getDocumentId()) );
		 
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
		 
     	 
		CarteRecup c =carteRecupRepository.save(CarteRecupConverter.dtoToEntity(carteRecupDto) );
		
		
		
		// Conversion de chaque élément de la liste en utilisant la méthode dtoToEntity
		List<AccusationCarteRecup> listAcc = carteRecupDto.getAccusationCarteRecups().stream()
				.map(dto -> {
					AccusationCarteRecupId id = new AccusationCarteRecupId();
					id.setIdEnfant(c.getDocumentId().getIdEnfant());
					id.setNumOrdinalArrestation(c.getDocumentId().getNumOrdinalArrestation());
					id.setNumOrdinalAffaire(c.getDocumentId().getNumOrdinalAffaire());
					id.setNumOrdinalDoc(c.getDocumentId().getNumOrdinalDoc());
					id.setNumOrdinalDocByAffaire(c.getDocumentId().getNumOrdinalDocByAffaire());
				
			        AccusationCarteRecup entity = AccusationCarteRecupConverter.dtoToEntity(dto);
			    	id.setIdTitreAccusation(entity.getTitreAccusation().getId()   );
			    	entity.setAccusationCarteRecupId(id);
 			        entity.setCarteRecup(c);
			         
			        return entity;
			    }).collect(Collectors.toList());
		
 		accusationCarteRecupRepository.saveAll(listAcc);
 		
 		AtomicInteger counter = new AtomicInteger(1); // Starting value for the increment
 		List<ArretProvisoire> listArretProvisoires = carteRecupDto.getArretProvisoires().stream()			   
 		.map(dto -> {
			
 			ArretProvisoireId id = new ArretProvisoireId();

 			id.setIdEnfant(c.getDocumentId().getIdEnfant());
			id.setNumOrdinalArrestation(c.getDocumentId().getNumOrdinalArrestation());
			id.setNumOrdinalAffaire(c.getDocumentId().getNumOrdinalAffaire());
			id.setNumOrdinalzDoc(c.getDocumentId().getNumOrdinalDoc());
			id.setNumOrdinalDocByAffaire(c.getDocumentId().getNumOrdinalDocByAffaire());
 			
			 id.setNumOrdinalArret(counter.getAndIncrement()); // Increment and set
			
 			ArretProvisoire entity = ArretProvisoireConverter.dtoToEntity(dto);
 			 entity.setArretProvisoireId(id);
	        entity.setCarteRecup(c);
	         
	        return entity;
	    }).collect(Collectors.toList());
 		
 		
 		arretProvisoireRepository.saveAll(listArretProvisoires) ;
 
		try {
			return  CarteRecupConverter.entityToDto(c) ;
		} catch (Exception e) {
			return   null ;
		}

	}

	@Override
	public  CarteRecupDto  update(  CarteRecupDto carteRecupDto) {
		try {
			
			CarteRecup  carteRecup = carteRecupRepository.save(CarteRecupConverter.dtoToEntity(carteRecupDto));

			return  CarteRecupConverter.entityToDto(carteRecup)   ;
		} catch (Exception e) {
			return   null ;
		}

	}
	 
	 
	
}