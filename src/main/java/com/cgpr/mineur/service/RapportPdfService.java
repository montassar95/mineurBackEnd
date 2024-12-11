package com.cgpr.mineur.service;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import com.cgpr.mineur.dto.EnfantDto;
import com.cgpr.mineur.dto.EnfantVerifieDto;
import com.cgpr.mineur.dto.ResidenceDto;
import com.cgpr.mineur.resource.EnfantAddDTO;
import com.cgpr.mineur.resource.EnfantDTO;
import com.cgpr.mineur.resource.PDFListExistDTO;
import com.cgpr.mineur.resource.PDFPenaleDTO;
 

 
 

public interface RapportPdfService {

	 
	 

	 
  	public ResponseEntity<InputStreamResource> genererRapportPdfMensuel(PDFListExistDTO pDFListExistDTO);
	public ResponseEntity<InputStreamResource> genererStatistiquePdfMensuel(PDFListExistDTO pDFListExistDTO);
  	public ResponseEntity<InputStreamResource> genererRapportPdfActuel( PDFListExistDTO pDFListExistDTO) throws IOException ;		 
	public ResponseEntity<InputStreamResource> genererFicheDeDetentionPdf( PDFPenaleDTO pDFPenaleDTO) ;
	
	
	 

	
 
}

