package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.EtabChangeManiere;

 
 


public interface EtabChangeManiereService   {
	 
	
	public List<EtabChangeManiere> listEtablissement() ;
	 
	
	public EtabChangeManiere getEtablissementById( String id);

	
	public EtabChangeManiere save( EtabChangeManiere etablissement);

	
	public EtabChangeManiere update( EtabChangeManiere etablissement);


	public Void delete( String id) ;
}

