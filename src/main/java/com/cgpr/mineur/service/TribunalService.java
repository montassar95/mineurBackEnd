package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Tribunal;

 
 


public interface TribunalService  {
	
	
	public List<Tribunal> listTribunal() ;

	
	public Tribunal getTribunalById( long id);
	
	
	public List<Tribunal> searchTribunal(long idGouv, long idType) ;

	
	public Tribunal save(Tribunal tribunal) ;

	
	public Tribunal update( Tribunal tribunal);

	
	public Void delete( long id) ;
}

