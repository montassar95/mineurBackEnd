package com.cgpr.mineur.service;


 
import java.util.List;

import org.springframework.data.repository.query.Param;

import com.cgpr.mineur.dto.AffaireData;
import com.cgpr.mineur.dto.AffaireDto;
import com.cgpr.mineur.dto.FicheDeDetentionDto;
import com.cgpr.mineur.dto.VerifierAffaireDto;
 
 

 


public interface AffaireService  {
 
	 
	public Object  calculerDateFin(  String date,  int duree) ;
	
	 
	
	public FicheDeDetentionDto obtenirInformationsDeDetentionParIdDetention(String idEnfant,  long numOrdinale) ;
	public  List<AffaireDto>  trouverAffairesParAction(  String action ,  String idEnfant, long numOrdinale)  ;
	public AffaireDto mettreAJourNumeroOrdinal(AffaireDto affaire ) ;
	
	
	public VerifierAffaireDto validerAffaire(AffaireData affaireData );
	
	
//	public boolean trouverDetenusParNumAffaireEtTribunalId( String numAffaire,   long tribunalId);
	 
	
	
	

	
	 
	
	 
}


