package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.CauseLiberationDto;


 


public interface CauseLiberationService   {
	 
	
	 

	 
		public List<CauseLiberationDto> listCauseLiberation() ;

		 
		public CauseLiberationDto getTypeAffaireById( long id);

		 
		public CauseLiberationDto save( CauseLiberationDto causeDecesDto) ;
		
		public CauseLiberationDto update(CauseLiberationDto causeDecesDto) ;

	 
		public Void delete( long id) ;
}

