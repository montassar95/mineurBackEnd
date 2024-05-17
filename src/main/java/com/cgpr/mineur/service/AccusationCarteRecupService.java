package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.AccusationCarteRecup;
import com.cgpr.mineur.models.CarteRecup;

 
 
                                                                                               

public interface AccusationCarteRecupService  {
	
	 
	public List<AccusationCarteRecup>  findByCarteRecup(  CarteRecup carteRecup) ;

 
	public Object  getArrestationById(  String date,  int duree) ;

	 
	public  AccusationCarteRecup  save(  AccusationCarteRecup accusationCarteRecup)  ;
}

