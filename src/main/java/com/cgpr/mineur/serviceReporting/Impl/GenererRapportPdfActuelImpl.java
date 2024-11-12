package com.cgpr.mineur.serviceReporting.Impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Etablissement;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.resource.PDFListExistDTO;
import com.cgpr.mineur.serviceReporting.ChargeAllEnfantService;
import com.cgpr.mineur.serviceReporting.GenererRapportPdfActuelService;
import com.cgpr.mineur.tools.ToolsForReporting;
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
    
    
    
    
    

    String titreString = ToolsForReporting.getTitreString(pDFListExistDTO.getEtatJuridiue());

    ByteArrayOutputStream out = new ByteArrayOutputStream();

    Document document = new Document(PageSize.A4.rotate(), 10f, 10f, 25f, 0f);
    PdfWriter.getInstance(document, out);
    document.open();

    PdfPTable tableTop = new PdfPTable(3);
    
    String date = ToolsForReporting.dtf.format(LocalDate.now());
    String gouvernorat = pDFListExistDTO.getEtablissement().getGouvernorat().getLibelle_gouvernorat().toString();
    try {
    	
    	
      tableTop = ToolsForReporting.createTopTable(gouvernorat,date);
    } catch (Exception e1) {

      e1.printStackTrace();
    }
      titreString = "قائمة إسمية للأطفال " + titreString + " بمركز ";
    List< Etablissement> etablissements = pDFListExistDTO.getEtablissements();

    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < etablissements.size(); i++) {
        builder.append(etablissements.get(i).getLibelle_etablissement().trim());
        
        if (i < etablissements.size() - 1) {
            builder.append(" و ");
        }
    }

    String resultatFinal = titreString + builder.toString();
    PdfPTable tTitre = ToolsForReporting.createTitleTable(resultatFinal);
    PdfPTable tableAffaire = new PdfPTable(100);
    tableAffaire.setWidthPercentage(100);

    ToolsForReporting.addCellToHeaderTable(tableAffaire, "القضايا", 41);
    ToolsForReporting.addCellToHeaderTable(tableAffaire, "تــــــاريخ", 19);
    ToolsForReporting.addCellToHeaderTable(tableAffaire, "الهوية", 28);
    ToolsForReporting.addCellToHeaderTable(tableAffaire, "ع.لإيقاف", 9);
    ToolsForReporting.addCellToHeaderTable(tableAffaire, "ع.ر", 3);

    // --------------------------------------------------------------------------------------------------------------------

    IntStream.range(0, residenceDTOList.size()).forEach(i -> 
    ToolsForReporting.processTablePrencipal(
        residenceDTOList.get(i),
        tableAffaire,
        pDFListExistDTO.getEtatJuridiue().toString(),
        i
    )
      );
    document.add(tableTop);
    document.add(tTitre);
    document.newPage();

    document.add(tableAffaire);

    document.close();

  
 
    
    return new ByteArrayInputStream(out.toByteArray());

  }

  

  

  

 

 

}