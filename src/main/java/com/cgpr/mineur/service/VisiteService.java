package com.cgpr.mineur.service;

import com.cgpr.mineur.dto.VisiteDto;

 
 


public interface VisiteService  {
	 
	
	public VisiteDto save(VisiteDto visite);
	
	public Void delete( long id);
	
	public VisiteDto getVisite( String id, int anneeVisite,int moisVisite);
}

