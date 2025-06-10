package com.cgpr.mineur.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.AffaireConverter;
import com.cgpr.mineur.converter.RefuseRevueConverter;
import com.cgpr.mineur.dto.RefuseRevueDto;
import com.cgpr.mineur.models.RefuseRevue;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.repository.RefuseRevueRepository;
import com.cgpr.mineur.service.RefuseRevueService;


 

@Service
public class RefuseRevueServiceImpl implements RefuseRevueService{

 
	@Autowired
	private RefuseRevueRepository refuseRevueRepository;

	@Autowired
	private AffaireRepository affaireRepository;

	@Autowired
	private ArrestationRepository arrestationRepository;

	@Autowired
	private DocumentRepository documentRepository;

	@Override
	public RefuseRevueDto save(  RefuseRevueDto refuseRevueDto) {
  System.out.println(refuseRevueDto.getAffaire().toString());
		if (refuseRevueDto.getAffaire().getAffaireLien() != null) {
			refuseRevueDto.getAffaire().getAffaireLien().setStatut(1);
			System.out.println("=========================debut lien ==================================");

			refuseRevueDto.getAffaire().setNumOrdinalAffaireByAffaire(
					refuseRevueDto.getAffaire().getAffaireLien().getNumOrdinalAffaireByAffaire() + 1);

			refuseRevueDto.getAffaire().setTypeDocument("CRR");
			refuseRevueDto.getAffaire().setTypeAffaire(refuseRevueDto.getAffaire().getAffaireLien().getTypeAffaire());

			// refuseRevue.getAffaire().setDaysDiffJuge(refuseRevue.getAffaire().getDaysDiffJuge());
			// refuseRevue.getAffaire().setDateDebutPunition(refuseRevue.getAffaire().getDateDebutPunition());
			// because i find date fin puntion where statut affair equalse 0
			refuseRevueDto.getAffaire()
					.setDateDebutPunition(refuseRevueDto.getAffaire().getAffaireLien().getDateDebutPunition());
			refuseRevueDto.getAffaire().setDateFinPunition(refuseRevueDto.getAffaire().getAffaireLien().getDateFinPunition());

			affaireRepository.save(AffaireConverter.dtoToEntity(refuseRevueDto.getAffaire().getAffaireLien()));
			System.out.println("============================fin lien===============================");
		}
		System.out.println("================================debut affaire ===========================");
		System.out.println(refuseRevueDto.getAffaire().toString());
		refuseRevueDto.getAffaire().setTypeDocument("CRR");

		affaireRepository.save(AffaireConverter.dtoToEntity(refuseRevueDto.getAffaire()));
		System.out.println("==================================fin affaire=========================");
		refuseRevueDto.setTypeAffaire(refuseRevueDto.getAffaire().getTypeAffaire());
		RefuseRevue c = refuseRevueRepository.save( RefuseRevueConverter.dtoToEntity(refuseRevueDto)  );

		try {
			 return RefuseRevueConverter.entityToDto(c);
			 
		} catch (Exception e) {
			return  null;
		}

	}
 
}

