package com.cgpr.mineur.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.AffaireConverter;
import com.cgpr.mineur.converter.OppositionConverter;
import com.cgpr.mineur.dto.OppositionDto;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.Opposition;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.OppositionRepository;
import com.cgpr.mineur.service.OppositionService;
@Service
public class OppositionServiceImpl  implements OppositionService {

	

	@Autowired
	private OppositionRepository oppositionRepository;

	@Autowired
	private AffaireRepository affaireRepository;

	 
	
	
	@Override
	public OppositionDto save(OppositionDto oppositionDto) {
		 

		oppositionDto.getAffaire().setTypeDocument("OPP");
		oppositionDto.getAffaire().setTypeAffaire(oppositionDto.getAffaire().getTypeAffaire());
		Affaire affaireSaved = affaireRepository.save(AffaireConverter.dtoToEntity(oppositionDto.getAffaire()));
	 	oppositionDto.getAffaire().setNumOrdinalAffaireByAffaire(affaireSaved.getNumOrdinalAffaireByAffaire());
		oppositionDto.setTypeAffaire(oppositionDto.getAffaire().getTypeAffaire());
		
	 
		
		Opposition c = oppositionRepository.save(OppositionConverter.dtoToEntity(oppositionDto));

 	try {
			return null ;
		} catch (Exception e) {
			return   null ;
		}
	}
	 
	 
}

