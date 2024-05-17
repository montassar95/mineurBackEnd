package com.cgpr.mineur.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public  class DocumentDto   {

	 

	 
	protected DocumentIdDto documentId;

	protected String typeDocument;
	private String typeDocumentActuelle;
	
 
	protected AffaireDto affaire;
	
	private Date dateEmission;

	private Date dateDepotCarte;
	
	 
	protected TypeAffaireDto typeAffaire;

	
	
	private String numArrestation;

	 

	 
	private EtablissementDto etablissement;
	
	
	 
	private PersonelleDto personelle;
	
	
	private Date dateInsertion;
 

	public DocumentDto(DocumentIdDto documentId, String typeDocument) {
		super();
		this.documentId = documentId;
		this.typeDocument = typeDocument;
	}


	 
	
	 
 
}