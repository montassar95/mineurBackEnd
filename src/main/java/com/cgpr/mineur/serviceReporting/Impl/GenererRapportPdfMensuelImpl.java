package com.cgpr.mineur.serviceReporting.Impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.config.ConfigShaping;
import com.cgpr.mineur.config.DetenuStatutDTO;
import com.cgpr.mineur.controllers.DetenuService;
//import com.cgpr.mineur.converter.RapportDetentionDTOConverter;
import com.cgpr.mineur.dto.RapportDetentionDTO;
import com.cgpr.mineur.models.Etablissement;
//import com.cgpr.mineur.models.rapport.RapportDetention;
//import com.cgpr.mineur.repository.rapport.RapportDetentionRepository;
import com.cgpr.mineur.resource.PDFListExistDTO;
import com.cgpr.mineur.serviceReporting.GenererRapportPdfMensuelService;
import com.cgpr.mineur.tools.ToolsForReporting;
import com.ibm.icu.text.ArabicShapingException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class GenererRapportPdfMensuelImpl implements GenererRapportPdfMensuelService {

 
//	private final RapportDetentionRepository rapportDetentionRepository;
	 private final DetenuService detenuService;
	 
	@Autowired
	public GenererRapportPdfMensuelImpl(  DetenuService detenuService) {
		 
//					this.rapportDetentionRepository = rapportDetentionRepository;
					this.detenuService = detenuService;
	 
	}

	 

	ConfigShaping boldConf = new ConfigShaping();

	 
	 
	@Override
	public ByteArrayInputStream genererRapportPdfMensuel(PDFListExistDTO pDFListExistDTO) throws DocumentException, IOException, ArabicShapingException {
	    List<String> etablissementIds = pDFListExistDTO.getEtablissements().stream()
	        .map(Etablissement::getId)
	        .collect(Collectors.toList());

	    List<String> statutPenals = Arrays.asList(pDFListExistDTO.getEtatJuridiue());
	    LocalDate date1 = LocalDate.parse(pDFListExistDTO.getDatePrintAllCentre());
	    LocalDate firstDayOfMonth = date1.withDayOfMonth(1);
	    // Optimiser la récupération des rapports avec une requête spécifique
//	    List<RapportDetention> rapports = rapportDetentionRepository.findByDateAndStatutPenalAndEtablissements(date1, statutPenals, etablissementIds);
	  
	    List<DetenuStatutDTO> rapports =  detenuService.getFilteredDetenus(firstDayOfMonth, date1, pDFListExistDTO.getEtablissements().get(0).getId(), pDFListExistDTO.getEtatJuridiue() );
	    System.out.println("Nombre de rapports: " + rapports.size());

	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    Document document = new Document(PageSize.A4.rotate(), 10f, 10f, 25f, 0f);
	    PdfWriter.getInstance(document, out);
	    document.open();
	    
	    PdfPTable tableTop = new PdfPTable(3);
	    
	    String date = ToolsForReporting.dtf.format(LocalDate.now());
	    System.out.println(pDFListExistDTO.toString());
// 	    String gouvernorat = pDFListExistDTO.getEtablissement().getGouvernorat().getLibelle_gouvernorat().toString();
	    
	    String gouvernorat = "تونس  " ;
	    try {
	    	
	    	
	      tableTop = ToolsForReporting.createTopTable(gouvernorat,date);
	    } catch (Exception e1) {

	      e1.printStackTrace();
	    }
	    
	    
	    int yearsVisite = date1.getYear();
		int moisVisite = date1.getMonthValue();
	     String titreString= getStatut( pDFListExistDTO.getEtatJuridiue())+"  بمركز إصــــلاح الأطفــــال الجــــانحين "+pDFListExistDTO.getEtablissements().get(0).getLibelle_etablissement()+" خلال شهر "
 				+ ToolsForReporting.getCurrentArabicMonth(moisVisite) + " " + yearsVisite; //localDate.getYear()
			    PdfPTable tTitre = ToolsForReporting.createTitleTable(titreString);
 	
		
 		tTitre.setSpacingBefore(50f);
 		document.add(tableTop);
 		
	    document.add(tTitre);
	    String totalDetenus = "المجموع  : " + rapports.size() ;
	    PdfPTable totalTable = ToolsForReporting.createTitleTableXX(totalDetenus);
	    totalTable.setSpacingBefore(20f);
	    document.add(totalTable);
		document.newPage();
	    // Créer une seule table pour tous les rapports
	    PdfPTable tableAffaire = new PdfPTable(100);
	    tableAffaire.setWidthPercentage(100);
	    ToolsForReporting.addCellToHeaderTable(tableAffaire, "القضايا", 41);
	    ToolsForReporting.addCellToHeaderTable(tableAffaire, "تــــــاريخ", 19);
	    ToolsForReporting.addCellToHeaderTable(tableAffaire, "الهوية", 28);
	    ToolsForReporting.addCellToHeaderTable(tableAffaire, "ع.لإيقاف", 9);
	    ToolsForReporting.addCellToHeaderTable(tableAffaire, "ع.ر", 3);

	    // Boucle classique pour itérer sur les rapports
	    for (int i = 0; i < rapports.size(); i++) {
//	        RapportDetentionDTO rapportDTO = RapportDetentionDTOConverter.rapportDetentionToRapportDetentionDTO(rapports.get(i));
 	        try {
	            ToolsForReporting.processTablePrencipal(
	            	 rapports.get(i),
	                tableAffaire,
	                pDFListExistDTO.getEtatJuridiue(),
	                i
	            );
	        } catch (Exception e) {
	        	
	            // Log et continue le traitement pour les autres rapports
	            System.err.println("Erreur lors du traitement du rapport à l'index " + rapports.get(i).getDetentionId() + ": " + e.getMessage());
	        }
	    }

	    document.add(tableAffaire);
	    document.close();

	    return new ByteArrayInputStream(out.toByteArray());
	}


	public String getStatut(String type) {
	    switch (type.toLowerCase()) {
	        case "arrete":
	            return "الموقوفين";
	        case "juge":
	            return "المحكومين";
	        case "libre":
	            return "السراحات";
	        default:
	            return "Type inconnu";
	    }
	}
 
	 
}
