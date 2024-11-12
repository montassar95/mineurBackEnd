package com.cgpr.mineur.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.dto.EnfantDto;
import com.cgpr.mineur.dto.EnfantVerifieDto;
import com.cgpr.mineur.dto.ResidenceDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.repository.RapportEnfantQuotidienRepository;
import com.cgpr.mineur.resource.EnfantAddDTO;
import com.cgpr.mineur.resource.EnfantDTO;
import com.cgpr.mineur.resource.PDFListExistDTO;
import com.cgpr.mineur.resource.PDFPenaleDTO;
import com.cgpr.mineur.service.EnfantService;
import com.cgpr.mineur.service.RapportPdfService;
import com.cgpr.mineur.serviceReporting.ChargeAllEnfantService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/rapportPdf")
public class RapportPdfController {
	
	
	@Autowired
	private RapportPdfService rapportPdfService;

 

	@PostMapping("/genererRapportPdfMensuel")
	public ResponseEntity<InputStreamResource> genererRapportPdfMensuel(@RequestBody PDFListExistDTO pDFListExistDTO) {
		 
		ResponseEntity<InputStreamResource> response = rapportPdfService.genererRapportPdfMensuel(pDFListExistDTO);
		return response;

	}
	
	@PostMapping("/genererRapportPdfActuel")
	public ResponseEntity<InputStreamResource> genererRapportPdfActuel(@RequestBody PDFListExistDTO pDFListExistDTO) {

		ResponseEntity<InputStreamResource> response = null;
		try {
			response = rapportPdfService.genererRapportPdfActuel(pDFListExistDTO);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;

	}
	
	@PostMapping("/genererFicheDeDetentionPdf")
	public ResponseEntity<InputStreamResource> genererFicheDeDetentionPdf(@RequestBody PDFPenaleDTO pDFPenaleDTO) {

		ResponseEntity<InputStreamResource> response = rapportPdfService.genererFicheDeDetentionPdf(pDFPenaleDTO);
		return response;
	}
	
	 

}
