package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Gouvernorat;


 


public interface GouvernoratService  {
	

	
	public List<Gouvernorat> list();
	
	public Gouvernorat getById( long id);

	
	
	
	
	public Gouvernorat save( Gouvernorat gouv) ;

	
	public Gouvernorat update( Gouvernorat gouv);

	
	public Void delete( long id);
	 
}

