package com.cgpr.mineur.service;

import java.util.List;

import com.cgpr.mineur.dto.EtabChangeManiereDto;

 
 


public interface EtabChangeManiereService   {
	 
	
	public List<EtabChangeManiereDto> listEtablissement() ;
	 
	
	public EtabChangeManiereDto getEtablissementById( String id);

	
	public EtabChangeManiereDto save( EtabChangeManiereDto etablissementDto);

	
	public EtabChangeManiereDto update( EtabChangeManiereDto etablissementDto);


	public Void delete( String id) ;
}

