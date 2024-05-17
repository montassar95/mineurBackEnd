package com.cgpr.mineur.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	@Autowired
	private ArrestationRepository arrestationRepository;

	@Autowired
	private DocumentRepository documentRepository;
	
	
	@Override
	public AppelEnfant save(AppelEnfant appelEnfant) {
		System.out.println(appelEnfant.toString());

		System.out.println(appelEnfant.getDocumentId().toString());

		System.out.println("================================debut affaire ===========================");
		System.out.println(appelEnfant.getAffaire().toString());
		appelEnfant.getAffaire().setTypeDocument("AE");
		appelEnfant.getAffaire().setTypeAffaire(appelEnfant.getAffaire().getTypeAffaire());
		affaireRepository.save(appelEnfant.getAffaire());
		System.out.println("==================================fin affaire=========================");
		appelEnfant.getAffaire().setNumOrdinalAffaireByAffaire(3);
		appelEnfant.setTypeAffaire(appelEnfant.getAffaire().getTypeAffaire());
		AppelEnfant c = appelEnfantRepository.save(appelEnfant);

 	try {
			return null ;
		} catch (Exception e) {
			return   null ;
		}
	}
	 
	 
}

