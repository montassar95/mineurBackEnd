package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Arrestation;


 


public interface ArrestationService  {
	
 
	public  List<Arrestation>  list() ;

	 
	public  Arrestation  getArrestationById(  String idEnfant,  long numOrdinale) ;

	 
	public  Arrestation  findByIdEnfantAndStatut0(  String id)  ;

 
	 
	public List<Arrestation>  findByIdEnfant(  String id) ;

	 
	public  Object  countByEnfant( String id) ;

 
	public  Arrestation  save(  Arrestation arrestation)  ;
 
	
	public  Arrestation  update( Arrestation arrestation) ;

	 
	public  Arrestation  delete(  Arrestation arrestation) ;
}

