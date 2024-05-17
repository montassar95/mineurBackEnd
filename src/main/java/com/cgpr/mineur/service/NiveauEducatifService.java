package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Etablissement;
import com.cgpr.mineur.models.NiveauEducatif;

 
 


public interface NiveauEducatifService {

	
	public List<NiveauEducatif> listNiveauEducatif() ;


	public NiveauEducatif getNiveauEducatifById( long id) ;

	
	public NiveauEducatif save( NiveauEducatif niveauEducatif);

	
	public NiveauEducatif update(NiveauEducatif niveauEducatif) ;

	
	public Void delete( long id) ;
}

