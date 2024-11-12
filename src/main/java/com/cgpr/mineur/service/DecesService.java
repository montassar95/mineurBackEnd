package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.DecesDto;


 

public interface DecesService {


	public  List<DecesDto>  list() ;

	 
	public  DecesDto  getById(long id);
	
 
	public  DecesDto  save(DecesDto decesDto) ;
	
	

	 

	 

 
	public DecesDto update(DecesDto causeDecesDto) ;

	 
	public Void delete( long id);
}

