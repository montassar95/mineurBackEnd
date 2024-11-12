package com.cgpr.mineur.service;


 
import java.util.List;

import com.cgpr.mineur.dto.EchappesDto;


 

public interface EchappesService  {
	 
	
 
	public EchappesDto save(EchappesDto echappesDto) ;
	public List<EchappesDto> trouverEchappesParIdDetenuEtNumDetention( String idEnfant, long numOrdinaleArrestation) ;
	 
 

 
	
}

