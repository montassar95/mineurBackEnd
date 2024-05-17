package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Etablissement;

 
 


public interface EtablissementService   {
 
	
	public List<Etablissement> listEtablissement() ;

	public List<Etablissement> listEtablissementCentre();
	

	public Etablissement getEtablissementById( String id) ;


	public Etablissement save( Etablissement etablissement);


	public Etablissement update( Etablissement etablissement); 

	
	public Void delete( String id);
}

