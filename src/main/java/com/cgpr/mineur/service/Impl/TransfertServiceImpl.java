package com.cgpr.mineur.service.Impl;


 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public  Transfert  save(  Transfert transfert) {

		System.out.println(transfert.toString());

		System.out.println(transfert.getDocumentId().toString());

		if (transfert.getAffaire().getAffaireLien() != null) {
			transfert.getAffaire().getAffaireLien().setStatut(2);
			System.out.println("=========================debut lien ==================================");
			System.out.println(transfert.getAffaire().getAffaireLien().toString());
			transfert.getAffaire().setNumOrdinalAffaireByAffaire(
					transfert.getAffaire().getAffaireLien().getNumOrdinalAffaireByAffaire() + 1);

			transfert.getAffaire().setTypeDocument("T");
			transfert.getAffaire().setTypeAffaire(transfert.getAffaire().getAffaireLien().getTypeAffaire());
			affaireRepository.save(transfert.getAffaire().getAffaireLien());
			System.out.println("============================fin lien===============================");
		}
		System.out.println("================================debut affaire ===========================");
		System.out.println(transfert.getAffaire().toString());
		transfert.getAffaire().setTypeDocument("T");
		affaireRepository.save(transfert.getAffaire());
		System.out.println("==================================fin affaire=========================");

		transfert.setTypeAffaire(transfert.getAffaire().getTypeAffaire());

		System.out.println(transfert);
		Transfert c = transfertRepository.save(transfert);

		try {
			return   null ;
		} catch (Exception e) {
			return  null ;
		}

	}
	 
}