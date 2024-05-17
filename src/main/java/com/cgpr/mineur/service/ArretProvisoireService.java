package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.ArretProvisoire;
import com.cgpr.mineur.models.CarteRecup;


 


public interface ArretProvisoireService {

	 
	public  List<ArretProvisoire>  list();  
 

	 
	public  ArretProvisoire  save( ArretProvisoire arretProvisoire) ; 

	 
	 ArretProvisoire  update(  ArretProvisoire arretProvisoire) ;
	
	
	
	
	 
	public   List<ArretProvisoire>  getDocumentByArrestation(  String idEnfant,  long numOrdinalArrestation) ;
	
	

	 
	public  List<ArretProvisoire>  findArretProvisoireByCarteRecup(  CarteRecup carteRecup)  ;
	
}

