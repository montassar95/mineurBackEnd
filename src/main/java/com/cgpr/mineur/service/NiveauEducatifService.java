package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.NiveauEducatifDto;

 
 


public interface NiveauEducatifService {

	
	public List<NiveauEducatifDto> listNiveauEducatif() ;


	public NiveauEducatifDto getNiveauEducatifById( long id) ;

	
	public NiveauEducatifDto save( NiveauEducatifDto niveauEducatifDto);

	
	public NiveauEducatifDto update(NiveauEducatifDto niveauEducatifDto) ;

	
	public Void delete( long id) ;
}

