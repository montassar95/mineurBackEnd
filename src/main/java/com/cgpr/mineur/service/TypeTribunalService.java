package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.TypeTribunal;


 


public interface TypeTribunalService {
	
	
	public List<TypeTribunal> list() ;


	public TypeTribunal getById( long id) ;
	
	
	
	 


	public TypeTribunal save(TypeTribunal causeDeces);


	public TypeTribunal update(TypeTribunal causeDeces);


	
	public Void delete(long id) ;

 
	 
}

