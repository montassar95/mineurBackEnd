package com.cgpr.mineur.service;


 
import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.CarteRecup;
 
 
 

public interface CarteRecupService   {
	 
	  
	 
	
	public  List<CarteRecup>  listEtablissement() ;

 

	public  CarteRecup  save(  CarteRecup carteRecup) ;

	 
	public CarteRecup update(CarteRecup carteRecup) ;
	 
	
}