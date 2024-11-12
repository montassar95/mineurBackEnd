package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.LiberationDto;


 


public interface LiberationService  {
	 
 
	public List<LiberationDto> listLiberation() ;
 

 
	public LiberationDto getLiberationById(String idEnfant,long numOrdinale);
	 
}

