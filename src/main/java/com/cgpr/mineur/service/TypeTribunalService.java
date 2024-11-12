package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.TypeTribunalDto;


 


public interface TypeTribunalService {
	
	
	public List<TypeTribunalDto> list() ;


	public TypeTribunalDto getById( long id) ;
	
	
	
	 


	public TypeTribunalDto save(TypeTribunalDto causeDeces);


	public TypeTribunalDto update(TypeTribunalDto causeDeces);


	
	public Void delete(long id) ;

 
	 
}

