package com.cgpr.mineur.service;


 
import java.util.List;

import com.cgpr.mineur.dto.CarteRecupDto;
 
 
 

public interface CarteRecupService   {
	 
	  
	 
	
	public  List<CarteRecupDto>  listEtablissement() ;

 

	public  CarteRecupDto  save(  CarteRecupDto carteRecupDto) ;

	 
	public CarteRecupDto update(CarteRecupDto carteRecupDto) ;
	 
	
}