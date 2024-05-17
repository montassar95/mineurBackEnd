package com.cgpr.mineur.service;


 
import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.dto.CalculeAffaireDto;
import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.ChangementLieu;
import com.cgpr.mineur.models.Document;
import com.cgpr.mineur.models.DocumentId;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.models.TitreAccusation;
 
 
 


 


public interface DocumentService {


	public List<Document> listAffaire();

	
	public Document findDocumentById(DocumentId documentId) ;

	
	public List<Document> getDocumentByAffaire( String idEnfant,
			 long numOrdinalArrestation,
			 long numOrdinalAffaire);

	
	public List<TitreAccusation> getTitreAccusation(String idEnfant,
			long numOrdinalArrestation,
			 long numOrdinalAffaire) ;
	
	public Object getDocumentByArrestation(String idEnfant,
			 long numOrdinalArrestation) ;

	
	public Object countDocumentByAffaire( String idEnfant,
			long numOrdinalArrestation,
			 long numOrdinalAffaire);

	
	public Affaire findByArrestation(String idEnfant) ;

	
	public Object findDocumentByArrestation( String idEnfant,
			long numOrdinalArrestation);

	
	public Integer delete( DocumentId documentId, String type);
	
	
	 
	 
}

