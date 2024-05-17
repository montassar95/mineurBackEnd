package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Nationalite;

 
 


public interface NationaliteService {

	
	public List<Nationalite> listNationalite();

	
	public Nationalite getNationaliteById( long id);


	public Nationalite save( Nationalite nationalite) ;

	
	public Nationalite update(Nationalite nationalite);

	
	public Void delete( long id) ;
}

