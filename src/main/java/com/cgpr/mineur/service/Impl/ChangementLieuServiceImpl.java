package com.cgpr.mineur.service.Impl;


 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.converter.AffaireConverter;
import com.cgpr.mineur.converter.ChangementLieuConverter;
import com.cgpr.mineur.dto.ChangementLieuDto;
import com.cgpr.mineur.models.ChangementLieu;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.ChangementLieuRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.service.ChangementLieuService;
 
 
 
@Service
public class ChangementLieuServiceImpl implements ChangementLieuService{

	@Autowired
	private ChangementLieuRepository changementLieuRepository;

	@Autowired
	private AffaireRepository affaireRepository;

	 
 
	@Autowired
	private ArrestationRepository arrestationRepository;
 
	
	@Autowired
	private  DocumentRepository documentRepository;

 
	@Override
	public  ChangementLieuDto  save( ChangementLieuDto changementLieuDto) {

		 
	 System.err.println("hello ");
	 System.out.println(changementLieuDto.toString());

		if (changementLieuDto.getAffaire().getAffaireLien() != null) {
			changementLieuDto.getAffaire().getAffaireLien().setStatut(1);
			System.out.println("=========================debut lien ==================================");
		 
			changementLieuDto.getAffaire().setNumOrdinalAffaireByAffaire(
			 changementLieuDto.getAffaire().getAffaireLien().getNumOrdinalAffaireByAffaire() + 1);
			
		 	//changementLieu.getAffaire().setTypeDocument(changementLieu.getAffaire().getAffaireLien().getTypeDocument().toString());
			changementLieuDto.setTypeDocumentActuelle("CHL");
		 	
			changementLieuDto.getAffaire().setTypeAffaire(changementLieuDto.getAffaire().getAffaireLien().getTypeAffaire());
			changementLieuDto.getAffaire().setTypeDocument(changementLieuDto.getTypeDocument());
			affaireRepository.save(AffaireConverter.dtoToEntity( changementLieuDto.getAffaire().getAffaireLien()));
			System.out.println("============================fin lien===============================");
		}
		
		
		
		
		System.out.println("================================debut affaire ===========================");
		System.out.println(changementLieuDto.toString());
		
		changementLieuDto.getAffaire().setTypeDocumentActuelle("CHL");
		System.out.println(changementLieuDto.getAffaire().toString());
		//affaireRepository.save(changementLieu.getAffaire());
		System.out.println("==================================fin affaire=========================");
		changementLieuDto.setTypeAffaire(changementLieuDto.getAffaire().getTypeAffaire());
		changementLieuDto.getAffaire().setTypeDocument(changementLieuDto.getTypeDocument());
 		affaireRepository.save(AffaireConverter.dtoToEntity(changementLieuDto.getAffaire()));
 		
		changementLieuDto.getAffaire().setTypeDocumentActuelle("CHL");
		changementLieuDto.setTypeDocumentActuelle("CHL");
		System.out.println(changementLieuDto.toString());
 	ChangementLieu c = changementLieuRepository.save(ChangementLieuConverter.dtoToEntity(changementLieuDto));
		
 
//		arrestationRepository.save(ar);

		try {
			return   null ;
		} catch (Exception e) {
			return   null ;
		}

	}
	 
	 
	 
	 
	 
	 
	
}