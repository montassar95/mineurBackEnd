package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.ClassePenaleDto;


 


public interface ClassePenaleService  {
	 
	
		 

	 
		public List<ClassePenaleDto>  list() ;

		 
		public  ClassePenaleDto  getById(  long id);
		
		
	 

		 
		public  ClassePenaleDto  save(  ClassePenaleDto causeDecesDto) ;

		 
		public ClassePenaleDto update(ClassePenaleDto causeDecesDto);

		 
		public  Void  delete(long id)  ;
}

