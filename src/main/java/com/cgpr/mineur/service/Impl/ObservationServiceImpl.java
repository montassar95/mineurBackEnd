package com.cgpr.mineur.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.AffaireConverter;
import com.cgpr.mineur.converter.ObservationConverter;
import com.cgpr.mineur.dto.ObservationDto;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.Observation;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ObservationRepository;
import com.cgpr.mineur.service.ObservationService;
@Service
public class ObservationServiceImpl  implements ObservationService {

	

	@Autowired
	private ObservationRepository observationRepository;

	@Autowired
	private AffaireRepository affaireRepository;

	 
	
	
	@Override
	public ObservationDto save(ObservationDto observationDto) {
		 

		observationDto.getAffaire().setTypeDocument("OBS");
		observationDto.getAffaire().setTypeAffaire(observationDto.getAffaire().getTypeAffaire());
		Affaire affaireSaved = affaireRepository.save(AffaireConverter.dtoToEntity(observationDto.getAffaire()));
	 	observationDto.getAffaire().setNumOrdinalAffaireByAffaire(affaireSaved.getNumOrdinalAffaireByAffaire());
		observationDto.setTypeAffaire(observationDto.getAffaire().getTypeAffaire());
		
	 
		
		Observation c = observationRepository.save(ObservationConverter.dtoToEntity(observationDto));

 	try {
			return null ;
		} catch (Exception e) {
			return   null ;
		}
	}
	 
	 
}

