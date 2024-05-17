package com.cgpr.mineur.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public Arreterlexecution save(Arreterlexecution arreterlexecution) {
		if (arreterlexecution.getAffaire().getAffaireLien() != null) {
			arreterlexecution.getAffaire().getAffaireLien().setStatut(1);
			System.out.println("=========================debut lien ==================================");

			arreterlexecution.getAffaire().setNumOrdinalAffaireByAffaire(
					arreterlexecution.getAffaire().getAffaireLien().getNumOrdinalAffaireByAffaire() + 1);

			arreterlexecution.getAffaire().setTypeDocument("AEX");
			arreterlexecution.getAffaire()
					.setTypeAffaire(arreterlexecution.getAffaire().getAffaireLien().getTypeAffaire());
			affaireRepository.save(arreterlexecution.getAffaire().getAffaireLien());
			System.out.println("============================fin lien===============================");
		}
		System.out.println("================================debut affaire ===========================");
		System.out.println(arreterlexecution.getAffaire().toString());
		arreterlexecution.getAffaire().setTypeDocument("AEX");
		affaireRepository.save(arreterlexecution.getAffaire());
		System.out.println("==================================fin affaire=========================");
		arreterlexecution.setTypeAffaire(arreterlexecution.getAffaire().getTypeAffaire());
		Arreterlexecution c = arreterlexecutionRepository.save(arreterlexecution);

//		arrestationRepository.save(ar);

		try {
			return  null ;
		} catch (Exception e) {
			return  null ;
		}

	}

}
