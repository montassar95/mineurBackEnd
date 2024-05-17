package com.cgpr.mineur.service.Impl;


 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public  ChangementLieu  save( ChangementLieu changementLieu) {

		 
	 

		if (changementLieu.getAffaire().getAffaireLien() != null) {
			changementLieu.getAffaire().getAffaireLien().setStatut(1);
			System.out.println("=========================debut lien ==================================");
		 
			changementLieu.getAffaire().setNumOrdinalAffaireByAffaire(
					changementLieu.getAffaire().getAffaireLien().getNumOrdinalAffaireByAffaire() + 1);
			
		 	//changementLieu.getAffaire().setTypeDocument(changementLieu.getAffaire().getAffaireLien().getTypeDocument().toString());
			changementLieu.setTypeDocumentActuelle("CHL");
		 	
			changementLieu.getAffaire().setTypeAffaire(changementLieu.getAffaire().getAffaireLien().getTypeAffaire());
			affaireRepository.save(changementLieu.getAffaire().getAffaireLien());
			System.out.println("============================fin lien===============================");
		}
		
		
		
		
		System.out.println("================================debut affaire ===========================");
		System.out.println(changementLieu.getAffaire().toString());
		
		 changementLieu.getAffaire().setTypeDocumentActuelle("CHL");
		//affaireRepository.save(changementLieu.getAffaire());
		System.out.println("==================================fin affaire=========================");
	 changementLieu.setTypeAffaire(changementLieu.getAffaire().getTypeAffaire());
		affaireRepository.save(changementLieu.getAffaire());
		changementLieu.getAffaire().setTypeDocumentActuelle("CHL");
		changementLieu.setTypeDocumentActuelle("CHL");
		System.out.println(changementLieu.toString());
		ChangementLieu c = changementLieuRepository.save(changementLieu);
		
 
//		arrestationRepository.save(ar);

		try {
			return   null ;
		} catch (Exception e) {
			return   null ;
		}

	}
	 
	 
	 
	 
	 
	 
	
}