package com.cgpr.mineur.service.Impl;


 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.AffaireConverter;
import com.cgpr.mineur.converter.TransfertConverter;
import com.cgpr.mineur.dto.TransfertDto;
import com.cgpr.mineur.models.Transfert;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.repository.TransfertRepository;
import com.cgpr.mineur.service.TransfertService;
 
 
 
@Service
public class TransfertServiceImpl implements TransfertService {

	
	@Autowired
	private TransfertRepository transfertRepository;

	@Autowired
	private AffaireRepository affaireRepository;

	@Autowired
	private ArrestationRepository arrestationRepository;

	@Autowired
	private DocumentRepository documentRepository;

	@Override
	public  TransfertDto  save(  TransfertDto transfertDto) {

		System.out.println(transfertDto.toString());

		System.out.println(transfertDto.getDocumentId().toString());

		if (transfertDto.getAffaire().getAffaireLien() != null) {
			transfertDto.getAffaire().getAffaireLien().setStatut(2);
			System.out.println("=========================debut lien ==================================");
			System.out.println(transfertDto.getAffaire().getAffaireLien().toString());
			transfertDto.getAffaire().setNumOrdinalAffaireByAffaire(
					transfertDto.getAffaire().getAffaireLien().getNumOrdinalAffaireByAffaire() + 1);

			transfertDto.getAffaire().setTypeDocument("T");
			transfertDto.getAffaire().setTypeAffaire(transfertDto.getAffaire().getAffaireLien().getTypeAffaire());
			affaireRepository.save( AffaireConverter.dtoToEntity(transfertDto.getAffaire().getAffaireLien()));
			System.out.println("============================fin lien===============================");
		}
		System.out.println("================================debut affaire ===========================");
		System.out.println(transfertDto.getAffaire().toString());
		transfertDto.getAffaire().setTypeDocument("T");
		affaireRepository.save(AffaireConverter.dtoToEntity(transfertDto.getAffaire()));
		System.out.println("==================================fin affaire=========================");

		transfertDto.setTypeAffaire(transfertDto.getAffaire().getTypeAffaire());

		System.out.println(transfertDto);
		Transfert c = transfertRepository.save(TransfertConverter.dtoToEntity(transfertDto)  );

		try {
			return   null ;
		} catch (Exception e) {
			return  null ;
		}

	}
	 
}