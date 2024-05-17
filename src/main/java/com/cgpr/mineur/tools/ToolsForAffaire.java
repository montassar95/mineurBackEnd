//package com.cgpr.mineur.tools;
//
//import java.net.URL;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.Period;
//import java.time.format.DateTimeFormatter;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.function.Predicate;
//
//import com.cgpr.mineur.config.ConfigShaping;
//import com.cgpr.mineur.dto.CalculeAffaireDto;
//import com.cgpr.mineur.models.Affaire;
//import com.cgpr.mineur.models.Echappes;
//import com.cgpr.mineur.models.Enfant;
//import com.cgpr.mineur.models.Residence;
//import com.cgpr.mineur.models.TitreAccusation;
//import com.cgpr.mineur.resource.PDFListExistDTO;
//import com.cgpr.mineur.serviceReporting.EnfantService2;
//import com.ibm.icu.text.ArabicShapingException;
//import com.itextpdf.text.BaseColor;
//import com.itextpdf.text.Element;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.Image;
//import com.itextpdf.text.Phrase;
//import com.itextpdf.text.Rectangle;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfWriter;
//
//public class ToolsForAffaire {
//
//	
//	public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//
//	 
//	 
//	public static CalculeAffaireDto calculatePenalAndArretDto(Affaire element) {
//	    CalculeAffaireDto dto = new CalculeAffaireDto();
//	    
//	    if (
//	        element.getAffaireAffecter() == null &&
//	        !"AEX".equals(element.getTypeDocument()) &&
//	        (element.getTypeJuge() == null || element.getTypeJuge().getId() != 29)
//	    ) {
//	        dto.setJourPenal(dto.getJourPenal() + element.getJour());
//	        dto.setMoisPenal(dto.getMoisPenal() + element.getMois());
//	        dto.setMoisPenal(dto.getMoisPenal() + (int) Math.floor(dto.getJourPenal() / 30) * 1);
//	        dto.setJourPenal(dto.getJourPenal() - (int) Math.floor((dto.getJourPenal() % 365) / 30) * 30);
//	        dto.setJourPenal(dto.getJourPenal() - (int) Math.floor(dto.getJourPenal() / 365) * 365);
//	        dto.setAnneePenal(dto.getAnneePenal() + (int) Math.floor(element.getAnnee()));
//	        dto.setAnneePenal(dto.getAnneePenal() + (int) Math.floor(dto.getMoisPenal() / 12));
//	        dto.setMoisPenal(dto.getMoisPenal() - (int) Math.floor(dto.getMoisPenal() / 12) * 12);
//	    }
//
//	    dto.setJourArret(dto.getJourArret() + element.getJourArret());
//	    dto.setMoisArret(dto.getMoisArret() + element.getMoisArret());
//	    dto.setMoisArret(dto.getMoisArret() + (int) Math.floor(dto.getJourArret() / 30) * 1);
//	    dto.setJourArret(dto.getJourArret() - (int) Math.floor((dto.getJourArret() % 365) / 30) * 30);
//	    dto.setJourArret(dto.getJourArret() - (int) Math.floor(dto.getJourArret() / 365) * 365);
//	    dto.setAnneeArret(dto.getAnneeArret() + (int) Math.floor(element.getAnneeArret()));
//	    dto.setAnneeArret(dto.getAnneeArret() + (int) Math.floor(dto.getMoisArret() / 12));
//	    dto.setMoisArret(dto.getMoisArret() - (int) Math.floor(dto.getMoisArret() / 12) * 12);
//
//	    dto.setDateJugementPrincipale(dateFormat.format(element.getDateEmission()));
//	    
//	    return dto;
//	}
//
//	
//	public static  CalculeAffaireDto processAppelParquetAndAppelEnfant(Affaire element) {
//	    CalculeAffaireDto dto = calculatePenalAndArretDto(element);
//
//	    if ("AP".equals(element.getTypeDocument())) {
//	        dto.setDateAppelParquet(dateFormat.format(element.getDateEmissionDocument() ));
//	        dto.setAppelParquet(true);
//	        dto.setDateJuge(true);
//	    }
//
//	    if ("AE".equals(element.getTypeDocument())) {
//	    	dto.setDateAppelEnfant(dateFormat.format(element.getDateEmissionDocument()));
//	        dto.setAppelEnfant(true);
//	        dto.setDateJuge(true);
//	      
//	    }
//
//	    return dto;
//	}
//	  
//	 
// 
////	public static CalculeAffaireDto checkForAdulte(Affaire element) {
////		 CalculeAffaireDto dto = new CalculeAffaireDto();
////	    if (element.getTypeJuge() != null && element.getTypeJuge().getId() == 4) {
////	    	dto.setAgeAdulte(true);;
////	    }
////		return dto;
////	    
////	}
//	 
//}
