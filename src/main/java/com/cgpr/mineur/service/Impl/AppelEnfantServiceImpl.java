package com.cgpr.mineur.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.AffaireConverter;
import com.cgpr.mineur.converter.AppelEnfantConverter;
import com.cgpr.mineur.dto.AppelEnfantDto;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.AppelEnfant;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.AppelEnfantRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.service.AppelEnfantService;
@Service
public class AppelEnfantServiceImpl  implements AppelEnfantService {

	

	@Autowired
	private AppelEnfantRepository appelEnfantRepository;

	@Autowired
	private AffaireRepository affaireRepository;

	 
	
	
	@Override
	public AppelEnfantDto save(AppelEnfantDto appelEnfantDto) {
		 

		appelEnfantDto.getAffaire().setTypeDocument("AE");
		appelEnfantDto.getAffaire().setTypeAffaire(appelEnfantDto.getAffaire().getTypeAffaire());
		Affaire affaireSaved = affaireRepository.save(AffaireConverter.dtoToEntity(appelEnfantDto.getAffaire()));
	 	appelEnfantDto.getAffaire().setNumOrdinalAffaireByAffaire(affaireSaved.getNumOrdinalAffaireByAffaire());
		appelEnfantDto.setTypeAffaire(appelEnfantDto.getAffaire().getTypeAffaire());
		
	 
		
		AppelEnfant c = appelEnfantRepository.save(AppelEnfantConverter.dtoToEntity(appelEnfantDto));

 	try {
			return null ;
		} catch (Exception e) {
			return   null ;
		}
	}
	 
	 
}

