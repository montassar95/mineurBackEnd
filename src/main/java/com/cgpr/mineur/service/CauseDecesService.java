package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.CauseDeces;


 


public interface CauseDecesService  {
	
 


 
	public  List<CauseDeces>  listCauseMutation() ;

 

	 
	public  CauseDeces  getTypeAffaireById(  long id) ;

 
	public  CauseDeces  save(  CauseDeces causeDeces)  ;

	 
	public CauseDeces  update(  CauseDeces causeDeces);

	 
	public  Void delete(  long id);
}

