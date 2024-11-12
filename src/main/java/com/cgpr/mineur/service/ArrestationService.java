package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.ArrestationDto;


 


public interface ArrestationService  {
	
 
	 

 	 
	public  ArrestationDto  trouverDerniereDetentionParIdDetenu(  String id)  ;

 
	 
 
	 
	public  Object  calculerNombreDetentionsParIdDetenu( String id) ;

 
	public  ArrestationDto  save(  ArrestationDto arrestationDto)  ;
 
	
 
	 
	public  ArrestationDto  delete(  ArrestationDto arrestationDto) ;
}

