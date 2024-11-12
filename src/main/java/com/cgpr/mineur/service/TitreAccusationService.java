package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.TitreAccusationDto;

 
 


public interface TitreAccusationService  {
	
 
	
	public List<TitreAccusationDto> findTitreAccusationByIdTypeAffaire( long id) ;

	
	public List<TitreAccusationDto> list() ;


	public TitreAccusationDto getById( long id);

	
	public TitreAccusationDto save( TitreAccusationDto causeDeces) ;


	
	public TitreAccusationDto update( TitreAccusationDto causeDeces);


	
	public Void delete( long id);
	 
}

