package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.CauseMutationDto;


 


public interface CauseMutationService   {

	
	public  List<CauseMutationDto>  listCauseMutation();
	
	public  CauseMutationDto  getTypeAffaireById(  long id);

	
	public  CauseMutationDto  save( CauseMutationDto causeDecesDto) ;
	
	public  CauseMutationDto  update(  CauseMutationDto causeDecesDto);

	
	public  Void  delete(  long id);
	 
}

