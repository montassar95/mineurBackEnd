package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.EtablissementDto;

 
 


public interface EtablissementService   {
 
	
	public List<EtablissementDto> listEtablissement() ;

	public List<EtablissementDto> listEtablissementCentre();
	

	public EtablissementDto getEtablissementById( String id) ;


	public EtablissementDto save( EtablissementDto etablissementDto);


	public EtablissementDto update( EtablissementDto etablissementDto); 

	
	public Void delete( String id);
}

