package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.CauseDecesDto;


 


public interface CauseDecesService  {
	
 


 
	public  List<CauseDecesDto>  listCauseMutation() ;

 

	 
	public  CauseDecesDto  getTypeAffaireById(  long id) ;

 
	public  CauseDecesDto  save(  CauseDecesDto causeDecesDto)  ;

	 
	public CauseDecesDto  update(  CauseDecesDto causeDecesDto);

	 
	public  Void delete(  long id);
}

