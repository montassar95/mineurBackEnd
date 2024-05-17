package com.cgpr.mineur.service;


 
import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.dto.CalculeAffaireDto;
import com.cgpr.mineur.models.Affaire;

 


public interface AffaireService  {
 
	 
	public  List<Affaire> listAffaire() ;

	 
	public  Affaire  getAffaireById(  String idEnfant,  String numAffaire,  long idTribunal,  long numOrdinaleArrestation) ;

 
	public  List<Affaire>  findAffaireByAnyArrestation(  String idEnfant,  String numAffaire,  long idTribunal);

	 
	public  Affaire  findAffaireByAffaireLien(  String idEnfant, String numAffaire,   long idTribunal) ;

 
	public  List<Affaire>  findByArrestation(  String idEnfant,   long numOrdinale);

	 
	public  List<Affaire>  findByArrestationToTransfert(  String idEnfant, long numOrdinale)  ;

	 
	public  List<Affaire>  findByArrestationToArret(  String idEnfant, long numOrdinale) ; 

 
	public  List<Affaire>  findByArrestationByCJorCR( String idEnfant,  long numOrdinale) ;

 
	public  List<Affaire>  findByArrestationByCDorCHorCP( String idEnfant,  long numOrdinale) ;

	
	public List<Affaire> findByNumOrdinalAffaire( String idEnfant, long numOrdinale,  long numOrdinalAffaire) ;

	 
	public Affaire verifierNumOrdinalAffaire(Affaire affaire, long numOrdinaleArrestationActuelle) ;

	
	public Affaire update( Affaire affaire) ;

	 
	public  Object  getDateDebutPunition(  String idEnfant,  long numOrdinale) ;

 
	public  Object getDateFinPunition( String idEnfant,  long numOrdinale);
	
	
	public CalculeAffaireDto calculerAffaire(String idEnfant,  long numOrdinale) ;
	
	 
	
	 
}


