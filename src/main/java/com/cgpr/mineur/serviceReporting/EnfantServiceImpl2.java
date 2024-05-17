package com.cgpr.mineur.serviceReporting;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.resource.PDFListExistDTO;
import com.cgpr.mineur.tools.ToolsForReporting;
import com.ibm.icu.text.ArabicShapingException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service

public class EnfantServiceImpl2 implements EnfantService2 {

  @Autowired
  private ChargeAllEnfantService chargeAllEnfantService;
  
  
  @Autowired
	RedisTemplate redisTemplate;
	
	private static String KEY = "MINEUR";

  @Override
  public ByteArrayInputStream exportEtat(PDFListExistDTO pDFListExistDTO)
  throws DocumentException, IOException, ArabicShapingException {

    System.err.println("exportEtat commancé  ");

    
//    List<Residence> residenceDTOList;
//    residenceDTOList = redisTemplate.opsForHash().values(KEY);
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
    titreString= "قائمة إسمية للأطفال " + titreString + " بمركز"
	+ pDFListExistDTO.getEtablissement().getLibelle_etablissement().toString().trim();
    PdfPTable tTitre = ToolsForReporting.createTitleTable(titreString);

    PdfPTable tableAffaire = new PdfPTable(100);
    tableAffaire.setWidthPercentage(100);

    ToolsForReporting.addCellToHeaderTable(tableAffaire, "القضايا", 41);
    ToolsForReporting.addCellToHeaderTable(tableAffaire, "تــــــاريخ", 19);
    ToolsForReporting.addCellToHeaderTable(tableAffaire, "الهوية", 28);
    ToolsForReporting.addCellToHeaderTable(tableAffaire, "ع.لإيقاف", 9);
    ToolsForReporting.addCellToHeaderTable(tableAffaire, "ع.ر", 3);

    // --------------------------------------------------------------------------------------------------------------------

    for (int i = 0; i < residenceDTOList.size(); i++) {
     
      
   //   redisTemplate.opsForHash().put(KEY, residenceDTOList.get(i).getArrestation().getArrestationId().getIdEnfant().toString(), residenceDTOList.get(i));
      ToolsForReporting.processTablePrencipal(
    		  residenceDTOList.get(i),
    		  tableAffaire, 
    		  pDFListExistDTO.getEtatJuridiue().toString(), 
    		  i);
    }

    document.add(tableTop);
    document.add(tTitre);
    document.newPage();

    document.add(tableAffaire);

    document.close();

    return new ByteArrayInputStream(out.toByteArray());

  }

  

  

  

 

 

}