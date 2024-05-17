package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.TitreAccusation;

 
 


public interface TitreAccusationService  {
	
 
	
	public List<TitreAccusation> findTitreAccusationByIdTypeAffaire( long id) ;

	
	public List<TitreAccusation> list() ;


	public TitreAccusation getById( long id);

	
	public TitreAccusation save( TitreAccusation causeDeces) ;


	
	public TitreAccusation update( TitreAccusation causeDeces);


	
	public Void delete( long id);
	 
}

