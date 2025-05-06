package com.cgpr.mineur.serviceReporting.Impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

//import com.cgpr.mineur.converter.RapportDetentionDTOConverter;
import com.cgpr.mineur.dto.RapportDetentionDTO;
import com.cgpr.mineur.models.Etablissement;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.resource.PDFListExistDTO;
import com.cgpr.mineur.serviceReporting.ChargeAllEnfantService;
import com.cgpr.mineur.serviceReporting.GenererRapportPdfActuelService;
import com.cgpr.mineur.tools.ToolsForReporting2;
import com.ibm.icu.text.ArabicShapingException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service

public class GenererRapportPdfActuelImpl implements GenererRapportPdfActuelService {

  @Autowired
  private ChargeAllEnfantService chargeAllEnfantService;
  
  
  @Override
  public ByteArrayInputStream genererRapportPdfActuel(PDFListExistDTO pDFListExistDTO)
  throws DocumentException, IOException, ArabicShapingException {

   
 
    List < Residence > residenceDTOList = chargeAllEnfantService.chargeSpecialList(pDFListExistDTO);
    
    
    
    
    

    String titreString = ToolsForReporting2.getTitreString(pDFListExistDTO.getEtatJuridiue());

    ByteArrayOutputStream out = new ByteArrayOutputStream();

    Document document = new Document(PageSize.A4.rotate(), 10f, 10f, 25f, 0f);
    PdfWriter.getInstance(document, out);
    document.open();

    PdfPTable tableTop = new PdfPTable(3);
    
    String date = ToolsForReporting2.dtf.format(LocalDate.now());
    String gouvernorat = pDFListExistDTO.getEtablissement().getGouvernorat().getLibelle_gouvernorat().toString();
    try {
    	
    	
      tableTop = ToolsForReporting2.createTopTable(gouvernorat,date);
    } catch (Exception e1) {

      e1.printStackTrace();
    }
     
      
      
      
    

    StringBuilder builder = new StringBuilder();
      if(!pDFListExistDTO.getEtatJuridiue().equals("detenusDeMemeAffaire")) {
    	  titreString = "قائمة إسمية للأطفال " + titreString + " بمركز ";
    	  List< Etablissement> etablissements = pDFListExistDTO.getEtablissements();
    	    for (int i = 0; i < etablissements.size(); i++) {
    	        builder.append(etablissements.get(i).getLibelle_etablissement().trim());
    	        
    	        if (i < etablissements.size() - 1) {
    	            builder.append(" و ");
    	        }
    	    }
      }
      else {
    	  titreString = "قائمة إسمية للأطفال " + titreString+pDFListExistDTO.getNumAffaire()  ;
      }
      

    String resultatFinal = titreString + builder.toString();
    PdfPTable tTitre = ToolsForReporting2.createTitleTable(resultatFinal);
    PdfPTable tableAffaire = new PdfPTable(100);
    tableAffaire.setWidthPercentage(100);

    ToolsForReporting2.addCellToHeaderTable(tableAffaire, "القضايا", 41);
    ToolsForReporting2.addCellToHeaderTable(tableAffaire, "تــــــاريخ", 19);
    ToolsForReporting2.addCellToHeaderTable(tableAffaire, "الهوية", 28);
    ToolsForReporting2.addCellToHeaderTable(tableAffaire, "ع.لإيقاف", 9);
    ToolsForReporting2.addCellToHeaderTable(tableAffaire, "ع.ر", 3);

    // --------------------------------------------------------------------------------------------------------------------
 // Convertir chaque résidence en un RapportDetentionDTO et traiter chaque rapport
//    List<RapportDetentionDTO> rapportDetentionDTOList = residenceDTOList.stream()
//        .map(RapportDetentionDTOConverter::residenceToRapportDetentionDTO)
//        .collect(Collectors.toList());

    // Traiter chaque rapport pour la table principale
    IntStream.range(0, residenceDTOList.size()).forEach(i -> {
       
        try {
        	ToolsForReporting2.processTablePrencipal(
        			residenceDTOList.get(i),
                tableAffaire,
                pDFListExistDTO.getEtatJuridiue().toString(),
                i
            );
        } catch (Exception e) {
            // Log et continue le traitement pour les autres rapports
            System.err.println("Erreur lors du traitement du rapport à l'index " + i + ": " + e.getMessage());
        }
    });
//    IntStream.range(0, residenceDTOList.size()).forEach(i -> 
//    ToolsForReporting2.processTablePrencipal(
//        residenceDTOList.get(i),
//        tableAffaire,
//        pDFListExistDTO.getEtatJuridiue().toString(),
//        i
//    )
//      );
    document.add(tableTop);
    document.add(tTitre);
    document.newPage();

    document.add(tableAffaire);

    document.close();

  
 
    
    return new ByteArrayInputStream(out.toByteArray());

  }


//  @Override
//  public List<RapportDetentionDTO> genererRapportJsonActuel(PDFListExistDTO pDFListExistDTO) {
//      // Charger la liste des résidences à partir du service
//      List<Residence> residenceDTOList = chargeAllEnfantService.chargeSpecialList(pDFListExistDTO);
//      
//      // Convertir chaque résidence en un RapportDetentionDTO
//      List<RapportDetentionDTO> rapportDetentionDTOList = residenceDTOList.stream()
//          .map(RapportDetentionDTOConverter::residenceToRapportDetentionDTO)
//          .collect(Collectors.toList());
//      
//      // Retourner la liste des RapportDetentionDTO
//      return rapportDetentionDTOList;
//  }

  

  

  

 

 

}