package com.cgpr.mineur.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.cgpr.mineur.models.Enfant;
import com.ibm.icu.text.ArabicShapingException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class EnfantPDFExporter {
 
     
	    public EnfantPDFExporter() {
	       
	    }
	 
	    
//	    public static final Font FONT = new Font();
//	    public static final Font BOLD = new Font(FontFamily.HELVETICA, 12, Font.BOLD);
//	    public ByteArrayInputStream export( Enfant enfant ) throws DocumentException, IOException, ArabicShapingException {
//	    	
//	    	
//	    	ByteArrayOutputStream out = new ByteArrayOutputStream();
//	    	
//	        Document document = new Document(PageSize.A4 );
//	        PdfWriter.getInstance(document, out );
// 
//	     
//	        document.open();
//	        ConfigShaping boldConf = new ConfigShaping();
//		       
//		    //Titre     
//		   Font boldfontTitle = boldConf.getFontForArabic(30);
//		   Font boldfontLabel = boldConf.getFontForArabic(16);
//           PdfPTable tTitre = new PdfPTable(1);	   
//           
//	       Phrase pTitre =  new Phrase(boldConf.format("مذكرة جزئية" ),boldfontTitle);
//	       PdfPCell cTitre = new PdfPCell(pTitre);
//	       cTitre.setHorizontalAlignment(Element.ALIGN_CENTER);
//	       cTitre.setBorderColor(BaseColor.WHITE);
//	       tTitre.addCell(cTitre);
//	       tTitre.setSpacingAfter(100f);
//	        
//	       
//	        
//	        PdfPTable table = new PdfPTable(2);
//	       // table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
//	        table.setWidthPercentage(100);
//	        
//	        
//	        Phrase p1;
//	        PdfPCell c1;
//	         
//	        PdfPCell spaceCell = new PdfPCell(new Phrase("  "));
//	        spaceCell.setBorderColor(BaseColor.WHITE);
//	      
////	       ---------------  nom --------------------
//	         p1 =  new Phrase(boldConf.format(enfant.getNom()),boldfontLabel);
//		     c1 = new PdfPCell(p1);
//		     c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		     c1.setBorderColor(BaseColor.WHITE);
//		     table.addCell(c1);
//	         
//	         
//	         p1 =  new Phrase(boldConf.format("الهوية" ),boldfontLabel);
//		     c1 = new PdfPCell(p1);
//		     c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		     c1.setBorderColor(BaseColor.WHITE);
//		     table.addCell(c1);
//		     table.addCell(spaceCell);
//		     table.addCell(spaceCell);
////		       ---------------  mere --------------------  
//		     
//	         p1 =  new Phrase(boldConf.format(enfant.getNomMere() ),boldfontLabel);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorderColor(BaseColor.WHITE);
//	         table.addCell(c1);
//		     
//	         p1 =  new Phrase(boldConf.format("هوية الأم" ),boldfontLabel);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorderColor(BaseColor.WHITE);
//	         table.addCell(c1);
//	         table.addCell(spaceCell);
//	         table.addCell(spaceCell);
// 
////		       ---------------  lieu --------------------	    
//	         
//	         p1 =  new Phrase(boldConf.format(enfant.getLieuNaissance() ),boldfontLabel);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorderColor(BaseColor.WHITE);
//	         table.addCell(c1);
//	         
//	         
//	         p1 =  new Phrase(boldConf.format("مكانها" ),boldfontLabel);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorderColor(BaseColor.WHITE);
//	         table.addCell(c1);
//	         table.addCell(spaceCell);
//	         table.addCell(spaceCell);
//	         
//	         
////		       ---------------  date --------------------      
//	         
//	         p1 =  new Phrase(boldConf.format(enfant.getDateNaissance().toString() ),boldfontLabel);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorderColor(BaseColor.WHITE);
//	         table.addCell(c1);
//	         
//	         
//	         p1 =  new Phrase(boldConf.format("تاريخ الولادة" ),boldfontLabel);
//	         c1 = new PdfPCell(p1);
//	         c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	         c1.setBorderColor(BaseColor.WHITE);
//	         table.addCell(c1);
//	         table.addCell(spaceCell);
//	         table.addCell(spaceCell);
// 
//	        document.add(tTitre);
//	        document.add(table);
//	        // step 5
//	        document.close();
//	       
//			return new ByteArrayInputStream(out.toByteArray());
//	            
//	   
//	    }
//	    
}
