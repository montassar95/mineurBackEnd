package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Liberation;


 


public interface LiberationService  {
	 
 
	public List<Liberation> listLiberation() ;
 

 
	public Liberation getLiberationById(String idEnfant,long numOrdinale);
	 
}

