package com.cgpr.mineur.service;


 
import java.util.List;

import com.cgpr.mineur.dto.AffaireDto;
import com.cgpr.mineur.dto.DocumentDto;
import com.cgpr.mineur.dto.DocumentIdDto;
import com.cgpr.mineur.dto.TitreAccusationDto;
 
 
 


 


public interface DocumentService {


 

	
	public DocumentDto trouverDocumentJudiciaireParId(DocumentIdDto documentIdDto) ;

	
	public List<DocumentDto> trouverDocumentsJudiciairesParEnfantEtDetentionEtAffaire( String idEnfant,
			 long numOrdinalArrestation,
			 long numOrdinalAffaire);

	
 
	
	public Object calculerNombreDocumentsJudiciairesParDetention(String idEnfant,
			 long numOrdinalArrestation) ;

	
	public Object calculerNombreDocumentsJudiciairesParAffaire( String idEnfant,
			long numOrdinalArrestation,
			 long numOrdinalAffaire);

	
	public AffaireDto trouverStatutJudiciaire(String idEnfant) ;

	
 

	
	public Integer delete( DocumentIdDto documentId, String type);
	
	
	 
	 
}

