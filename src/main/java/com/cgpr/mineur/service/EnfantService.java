package com.cgpr.mineur.service;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import com.cgpr.mineur.dto.EnfantDto;
import com.cgpr.mineur.dto.EnfantVerifieDto;
import com.cgpr.mineur.dto.ResidenceDto;
import com.cgpr.mineur.dto.SearchDetenuDto;
import com.cgpr.mineur.resource.EnfantAddDTO;
import com.cgpr.mineur.resource.EnfantDTO;
import com.cgpr.mineur.resource.PDFListExistDTO;
import com.cgpr.mineur.resource.PDFPenaleDTO;
 

 
 

public interface EnfantService {

	 
	 

	  
	
	
	
	
	


	public List<SearchDetenuDto> trouverResidencesParCriteresDetenu(EnfantDTO enfantDTO) ;
	public List<SearchDetenuDto> trouverDetenusParCriteresDansPrisons(EnfantDTO enfantDTO) ;
	

	public EnfantDto getEnfantById( String id) ;
	public EnfantVerifieDto trouverDetenuAvecSonStatutActuel(String id, String idEtab);
	
	public SearchDetenuDto trouverDerniereResidenceParIdDetenu( String id) ;

	
	public  List<SearchDetenuDto>  trouverResidencesParNumeroEcrou( String numArr);

	public ResidenceDto creerAdmissionDetenu( EnfantAddDTO enfantAddDTO) ;
	
	public ResidenceDto mettreAJourAdmissionDetenu( EnfantAddDTO enfantAddDTO) ;
	
	
	 


	
 
}

