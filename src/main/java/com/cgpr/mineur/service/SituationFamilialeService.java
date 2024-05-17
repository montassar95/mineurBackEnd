package com.cgpr.mineur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.SituationFamiliale;

 
 


public interface SituationFamilialeService  {


	
	public List<SituationFamiliale> listNationalite() ;


	public SituationFamiliale getById(long id);

	
	public SituationFamiliale save( SituationFamiliale situationFamiliale) ;

	
	public SituationFamiliale update(SituationFamiliale situationFamiliale) ;


	public Void delete(long id) ;
}

