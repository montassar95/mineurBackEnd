package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.ClassePenale;


 


public interface ClassePenaleService  {
	 
	
		 

	 
		public List<ClassePenale>  list() ;

		 
		public  ClassePenale  getById(  long id);
		
		
	 

		 
		public  ClassePenale  save(  ClassePenale causeDeces) ;

		 
		public ClassePenale update(ClassePenale causeDeces);

		 
		public  Void  delete(long id)  ;
}

