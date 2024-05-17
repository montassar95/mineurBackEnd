package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Metier;


 


public interface MetierService {
	
	 

	public List<Metier> list();

	public Metier getById( long id) ;

	
	
	

	public Metier save(Metier gouv);


	public Metier update( Metier gouv);


	public Void delete( long id) ;
}

