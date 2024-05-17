package com.cgpr.mineur.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public RefuseRevue save(  RefuseRevue refuseRevue) {

		if (refuseRevue.getAffaire().getAffaireLien() != null) {
			refuseRevue.getAffaire().getAffaireLien().setStatut(1);
			System.out.println("=========================debut lien ==================================");

			refuseRevue.getAffaire().setNumOrdinalAffaireByAffaire(
					refuseRevue.getAffaire().getAffaireLien().getNumOrdinalAffaireByAffaire() + 1);

			refuseRevue.getAffaire().setTypeDocument("CRR");
			refuseRevue.getAffaire().setTypeAffaire(refuseRevue.getAffaire().getAffaireLien().getTypeAffaire());

			// refuseRevue.getAffaire().setDaysDiffJuge(refuseRevue.getAffaire().getDaysDiffJuge());
			// refuseRevue.getAffaire().setDateDebutPunition(refuseRevue.getAffaire().getDateDebutPunition());
			// because i find date fin puntion where statut affair equalse 0
			refuseRevue.getAffaire()
					.setDateDebutPunition(refuseRevue.getAffaire().getAffaireLien().getDateDebutPunition());
			refuseRevue.getAffaire().setDateFinPunition(refuseRevue.getAffaire().getAffaireLien().getDateFinPunition());

			affaireRepository.save(refuseRevue.getAffaire().getAffaireLien());
			System.out.println("============================fin lien===============================");
		}
		System.out.println("================================debut affaire ===========================");
		System.out.println(refuseRevue.getAffaire().toString());
		refuseRevue.getAffaire().setTypeDocument("CRR");

		affaireRepository.save(refuseRevue.getAffaire());
		System.out.println("==================================fin affaire=========================");
		refuseRevue.setTypeAffaire(refuseRevue.getAffaire().getTypeAffaire());
		RefuseRevue c = refuseRevueRepository.save(refuseRevue);

		try {
			return  c;
		} catch (Exception e) {
			return  null;
		}

	}
 
}

