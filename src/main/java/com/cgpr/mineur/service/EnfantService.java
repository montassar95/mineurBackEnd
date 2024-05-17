package com.cgpr.mineur.service;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.dto.EnfantVerifieDto;
import com.cgpr.mineur.models.Enfant;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.resource.EnfantAddDTO;
import com.cgpr.mineur.resource.EnfantDTO;
import com.cgpr.mineur.resource.PDFListExistDTO;
import com.cgpr.mineur.resource.PDFPenaleDTO;
 

 
 

public interface EnfantService {

	 
	 

	 
	public ResponseEntity<InputStreamResource> exportAllEtat(PDFListExistDTO pDFListExistDTO);
			 
	 
	public List<Residence> getEnfants(EnfantDTO enfantDTO) ;

 

	 
	public ResponseEntity<InputStreamResource> exportToPDF( PDFPenaleDTO pDFPenaleDTO) ;


	public ResponseEntity<InputStreamResource> exportEtatToPDF( PDFListExistDTO pDFListExistDTO) throws IOException ;



	public EnfantVerifieDto chercherEnfantAvecVerification(String id);
	
	
	public List<Enfant> listEtablissement() ;

	
	public List<List<Residence>> listCharge() ;




	public Enfant getEnfantById( String id) ;


	public Residence getoneInResidence( String id) ;

	
	public  List<Residence>  getResidenceByNum( String numArr);


	public Enfant save(Enfant enfant,  String idEta);
	
	public Residence save( EnfantAddDTO enfantAddDTO) ;
	
	
	public Residence update( EnfantAddDTO enfantAddDTO) ;
	
	public Enfant update( Enfant enfant);

	
	public Void delete( String id);


	
 
}

