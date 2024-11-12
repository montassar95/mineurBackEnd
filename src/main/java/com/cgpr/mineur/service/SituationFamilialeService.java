package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.SituationFamilialeDto;

 
 


public interface SituationFamilialeService  {


	
	public List<SituationFamilialeDto> listNationalite() ;


	public SituationFamilialeDto getById(long id);

	
	public SituationFamilialeDto save( SituationFamilialeDto situationFamiliale) ;

	
	public SituationFamilialeDto update(SituationFamilialeDto situationFamiliale) ;


	public Void delete(long id) ;
}

