package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Deces;


 

public interface DecesService {


	public  List<Deces>  list() ;

	 
	public  Deces  getById(long id);
	
 
	public  Deces  save(Deces deces) ;
	
	

	 

	 

 
	public Deces update(Deces causeDeces) ;

	 
	public Void delete( long id);
}

