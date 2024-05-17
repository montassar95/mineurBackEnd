package com.cgpr.mineur.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.AppelParquet;
import com.cgpr.mineur.repository.AffaireRepository;
import com.cgpr.mineur.repository.AppelParquetRepository;
import com.cgpr.mineur.repository.ArrestationRepository;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.service.AppelParquetService;
@Service
public class AppelParquetServiceImpl implements AppelParquetService {

	@Autowired
	private AppelParquetRepository appelParquetRepository;

	@Autowired
	private AffaireRepository affaireRepository;

	@Autowired
	private ArrestationRepository arrestationRepository;

	@Autowired
	private DocumentRepository documentRepository;
	
	@Override
	public AppelParquet save(AppelParquet appelParquet) {

		System.out.println(appelParquet.toString());

		System.out.println(appelParquet.getDocumentId().toString());

		System.out.println("================================debut affaire ===========================");
		System.out.println(appelParquet.getAffaire().toString());
		appelParquet.getAffaire().setTypeDocument("AP");
		appelParquet.getAffaire().setTypeAffaire(appelParquet.getAffaire().getTypeAffaire());
		affaireRepository.save(appelParquet.getAffaire());
		System.out.println("==================================fin affaire=========================");
		appelParquet.getAffaire().setNumOrdinalAffaireByAffaire(3);
		appelParquet.setTypeAffaire(appelParquet.getAffaire().getTypeAffaire());
		AppelParquet c = appelParquetRepository.save(appelParquet);

		try {
			return  null ;
		} catch (Exception e) {
			return   null ;
		}
	}
	 
}

