package com.cgpr.mineur.service;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Visite;

 
 


public interface VisiteService  {
	 
	
	public Visite save(Visite visite);
	
	public Void delete( long id);
	
	public Visite getVisite( String id, int anneeVisite,int moisVisite);
}

