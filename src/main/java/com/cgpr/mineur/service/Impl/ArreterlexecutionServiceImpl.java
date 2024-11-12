package com.cgpr.mineur.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.AffaireConverter;
import com.cgpr.mineur.converter.ArreterlexecutionConverter;
import com.cgpr.mineur.dto.ArreterlexecutionDto;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.Arreterlexecution;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.ArreterlexecutionRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.service.ArreterlexecutionService;



@Service
public class ArreterlexecutionServiceImpl implements ArreterlexecutionService {

	
	@Autowired
	private ArreterlexecutionRepository arreterlexecutionRepository;

	@Autowired
	private AffaireRepository affaireRepository;

	@Autowired
	private ArrestationRepository arrestationRepository;

	@Autowired
	private DocumentRepository documentRepository;
	
	
	@Override
	public ArreterlexecutionDto save(ArreterlexecutionDto arreterlexecutionDto) {
		
		if (arreterlexecutionDto.getAffaire().getAffaireLien() != null) {
			arreterlexecutionDto.getAffaire().getAffaireLien().setStatut(1);
			System.out.println("=========================debut lien ==================================");

			arreterlexecutionDto.getAffaire().setNumOrdinalAffaireByAffaire(
					arreterlexecutionDto.getAffaire().getAffaireLien().getNumOrdinalAffaireByAffaire() + 1);

			arreterlexecutionDto.getAffaire().setTypeDocument("AEX");
			arreterlexecutionDto.getAffaire().setTypeAffaire(arreterlexecutionDto.getAffaire().getAffaireLien().getTypeAffaire());
		
			Affaire affaireSaved = affaireRepository.save(AffaireConverter.dtoToEntity(arreterlexecutionDto.getAffaire()));
			affaireRepository.save(AffaireConverter.dtoToEntity(arreterlexecutionDto.getAffaire()));
			System.out.println("============================fin lien===============================");
		}
		System.out.println("================================debut affaire ===========================");
		System.out.println(arreterlexecutionDto.getAffaire().toString());
		arreterlexecutionDto.getAffaire().setTypeDocument("AEX");
		System.out.println(AffaireConverter.dtoToEntity(arreterlexecutionDto.getAffaire()));
		affaireRepository.save(AffaireConverter.dtoToEntity(arreterlexecutionDto.getAffaire()));
		System.out.println("==================================fin affaire=========================");
		arreterlexecutionDto.setTypeAffaire(arreterlexecutionDto.getAffaire().getTypeAffaire());
		Arreterlexecution c = arreterlexecutionRepository.save(ArreterlexecutionConverter.dtoToEntity(arreterlexecutionDto));

//		arrestationRepository.save(ar);

		try {
			return  null ;
		} catch (Exception e) {
			return  null ;
		}

	}

}
