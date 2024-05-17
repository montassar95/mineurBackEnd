package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.CauseLiberation;


 


public interface CauseLiberationService   {
	 
	
	 

	 
		public List<CauseLiberation> listCauseLiberation() ;

		 
		public CauseLiberation getTypeAffaireById( long id);

		 
		public CauseLiberation save( CauseLiberation causeDeces) ;
		
		public CauseLiberation update(CauseLiberation causeDeces) ;

	 
		public Void delete( long id) ;
}

