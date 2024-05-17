package com.cgpr.mineur.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public Revue save( Revue revue) {

		if (revue.getAffaire().getAffaireLien() != null) {
			revue.getAffaire().getAffaireLien().setStatut(1);
			System.out.println("=========================debut lien ==================================");

			revue.getAffaire().setNumOrdinalAffaireByAffaire(
					revue.getAffaire().getAffaireLien().getNumOrdinalAffaireByAffaire() + 1);

			revue.getAffaire().setTypeDocument("CR");
			revue.getAffaire().setTypeAffaire(revue.getAffaire().getAffaireLien().getTypeAffaire());
			affaireRepository.save(revue.getAffaire().getAffaireLien());
			System.out.println("============================fin lien===============================");
		}
		System.out.println("================================debut affaire ===========================");
		System.out.println(revue.getAffaire().toString());
		revue.getAffaire().setTypeDocument("CR");
		affaireRepository.save(revue.getAffaire());
		System.out.println("==================================fin affaire=========================");
		revue.setTypeAffaire(revue.getAffaire().getTypeAffaire());
		Revue c = revueRepository.save(revue);

 
		try {
			return   c ;
		} catch (Exception e) {
			return   null ;
		}

	}

	 
}

