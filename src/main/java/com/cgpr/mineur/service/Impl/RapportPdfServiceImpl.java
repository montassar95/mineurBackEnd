package com.cgpr.mineur.service.Impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.dto.RapportDetentionDTO;
import com.cgpr.mineur.resource.PDFListExistDTO;
import com.cgpr.mineur.resource.PDFPenaleDTO;
import com.cgpr.mineur.service.RapportPdfService;
import com.cgpr.mineur.serviceReporting.GenererFicheDeDetentionPdfService;
import com.cgpr.mineur.serviceReporting.GenererRapportPdfActuelService;
import com.cgpr.mineur.serviceReporting.GenererRapportPdfMensuelService;
import com.cgpr.mineur.serviceReporting.GenererStatistiquePdfMensuelService;
import com.ibm.icu.text.ArabicShapingException;
import com.itextpdf.text.DocumentException;

@Service
public class RapportPdfServiceImpl implements RapportPdfService {

    private final GenererFicheDeDetentionPdfService genererFicheDeDetentionPdfService;
    private final GenererRapportPdfActuelService genererRapportPdfActuelService;
    private final GenererRapportPdfMensuelService genererRapportPdfMensuelService;
    private final GenererStatistiquePdfMensuelService genererStatistiquePdfMensuelService;

    @Autowired
    public RapportPdfServiceImpl(
            GenererFicheDeDetentionPdfService genererFicheDeDetentionPdfService,
            GenererRapportPdfActuelService genererRapportPdfActuelService,
            GenererRapportPdfMensuelService genererRapportPdfMensuelService,
            GenererStatistiquePdfMensuelService genererStatistiquePdfMensuelService) {
        this.genererFicheDeDetentionPdfService = genererFicheDeDetentionPdfService;
        this.genererRapportPdfActuelService = genererRapportPdfActuelService;
        this.genererRapportPdfMensuelService = genererRapportPdfMensuelService;
        this.genererStatistiquePdfMensuelService= genererStatistiquePdfMensuelService;
    }

    @Override
    public ResponseEntity<InputStreamResource> genererRapportPdfMensuel(PDFListExistDTO pDFListExistDTO) {
        try {
        	ByteArrayInputStream pdfBytes = genererRapportPdfMensuelService.genererRapportPdfMensuel(pDFListExistDTO);
            return createPdfResponse(pdfBytes, "rapport_mensuel.pdf");
        } catch (IOException | DocumentException | ArabicShapingException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build(); // Erreur serveur
        }
    }
    
    @Override
    public ResponseEntity<InputStreamResource> genererStatistiquePdfMensuel(PDFListExistDTO pDFListExistDTO) {
        try {
        	ByteArrayInputStream pdfBytes = genererStatistiquePdfMensuelService.genererStatistiquePdfMensuel(pDFListExistDTO);
            return createPdfResponse(pdfBytes, "statistique_mensuel.pdf");
        } catch (IOException | DocumentException | ArabicShapingException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build(); // Erreur serveur
        }
    }

    @Override
    public ResponseEntity<InputStreamResource> genererRapportPdfActuel(PDFListExistDTO pDFListExistDTO) {
        try {
            ByteArrayInputStream pdfStream = genererRapportPdfActuelService.genererRapportPdfActuel(pDFListExistDTO);
            return createPdfResponse(pdfStream, "rapport_actuel.pdf");
        } catch (IOException | DocumentException | ArabicShapingException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @Override
    public ResponseEntity<InputStreamResource> genererFicheDeDetentionPdf(PDFPenaleDTO pDFPenaleDTO) {
        try {
            ByteArrayInputStream pdfStream = genererFicheDeDetentionPdfService.genererFicheDeDetentionPdf(pDFPenaleDTO);
            return createPdfResponse(pdfStream, "fiche_detention.pdf");
        } catch (IOException | DocumentException | ArabicShapingException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

 

    private ResponseEntity<InputStreamResource> createPdfResponse(ByteArrayInputStream pdfStream, String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename);
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfStream));
    }

	@Override
	public List<RapportDetentionDTO> genererRapportJsonActuel(PDFListExistDTO pDFListExistDTO)  {
		 
			  List<RapportDetentionDTO> jsonStream = genererRapportPdfActuelService.genererRapportJsonActuel(pDFListExistDTO);
	            return jsonStream;
	       
		 
	}
}
