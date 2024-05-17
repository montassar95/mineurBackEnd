package com.cgpr.mineur.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Personelle;
 
 

public interface PersonelleService  {
	 
	

	public List<Personelle> listPersonelle(); 

	
	public Personelle save( Personelle personelle);
}