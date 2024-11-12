package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.NationaliteDto;

 
 


public interface NationaliteService {

	
	public List<NationaliteDto> listNationalite();

	
	public NationaliteDto getNationaliteById( long id);


	public NationaliteDto save( NationaliteDto nationalite) ;

	
	public NationaliteDto update(NationaliteDto nationalite);

	
	public Void delete( long id) ;
}

