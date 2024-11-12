package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.TribunalDto;

 
 


public interface TribunalService  {
	
	
	public List<TribunalDto> listTribunal() ;

	
	public TribunalDto getTribunalById( long id);
	
	
	public List<TribunalDto> searchTribunal(long idGouv, long idType) ;

	
	public TribunalDto save(TribunalDto tribunal) ;

	
	public TribunalDto update( TribunalDto tribunal);

	
	public Void delete( long id) ;
}

