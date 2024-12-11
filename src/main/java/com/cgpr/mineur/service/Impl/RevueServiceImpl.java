package com.cgpr.mineur.service.Impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.AffaireConverter;
import com.cgpr.mineur.converter.AffaireIdConverter;
import com.cgpr.mineur.converter.RevueConverter;
import com.cgpr.mineur.dto.RevueDto;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.Revue;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.repository.RevueRepository;
import com.cgpr.mineur.service.RevueService;


 

@Service
public class RevueServiceImpl implements RevueService{


	@Autowired
	private RevueRepository revueRepository;

	@Autowired
	private AffaireRepository affaireRepository;

	@Autowired
	private ArrestationRepository arrestationRepository;

	@Autowired
	private DocumentRepository documentRepository;

	@Override
	public RevueDto save( RevueDto revueDto) {
		System.out.println("revueDto");
		System.out.println(revueDto.toString());

		if (revueDto.getAffaire().getAffaireLien() != null) {
			revueDto.getAffaire().getAffaireLien().setStatut(1);
			 

			revueDto.getAffaire().setNumOrdinalAffaireByAffaire(
					revueDto.getAffaire().getAffaireLien().getNumOrdinalAffaireByAffaire() + 1);

			revueDto.getAffaire().setTypeDocument("CR");
			revueDto.getAffaire().setTypeAffaire(revueDto.getAffaire().getAffaireLien().getTypeAffaire());
			affaireRepository.save(AffaireConverter.dtoToEntity(revueDto.getAffaire().getAffaireLien()));
			 
		}
		 
		
		Optional<Affaire> affaire = affaireRepository.findById(AffaireIdConverter.dtoToEntity(revueDto.getAffaire().getAffaireId()));
		if(affaire.isPresent()) {
			affaire.get().setTypeDocument("CR");
			affaireRepository.save(affaire.get());
			
		}
		else {
			revueDto.getAffaire().setTypeDocument("CR");
			affaireRepository.save(AffaireConverter.dtoToEntity(revueDto.getAffaire()));
		}
		
	 
	 
		revueDto.setTypeAffaire(revueDto.getAffaire().getTypeAffaire());
		Revue c = revueRepository.save(RevueConverter.dtoToEntity(revueDto));

 
		try {
			return  RevueConverter.entityToDto(c)  ;
		} catch (Exception e) {
			return   null ;
		}

	}

	 
}

