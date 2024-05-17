package com.cgpr.mineur.service;


 
import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Echappes;


 

public interface EchappesService  {
	 
	
 
	public Echappes save(Echappes echappes) ;
	 
	public Object countByEnfant(String idEnfant,long numOrdinaleArrestation) ;

	 
	public Object countTotaleEchappes( String idEnfant,long numOrdinaleArrestation) ;
	 
	public Echappes findByIdEnfantAndResidenceTrouverNull( String idEnfant);

 
	public List<Echappes> findEchappesByIdEnfant( String idEnfant, long numOrdinaleArrestation) ;
}

