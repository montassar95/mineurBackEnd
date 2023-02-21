package com.cgpr.mineur.service;

 

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.cgpr.mineur.models.Enfant;
import com.cgpr.mineur.resource.EnfantDTO;
import com.cgpr.mineur.resource.PDFListExistDTO;
import com.cgpr.mineur.resource.PDFPenaleDTO;
import com.ibm.icu.text.ArabicShapingException;
import com.itextpdf.text.DocumentException;

 

public interface EnfantService {

	 
   // List<Enfant> getEnfants(EnfantDTO e);
    
    
    
    ByteArrayInputStream export( PDFPenaleDTO pDFPenaleDTO )throws DocumentException, IOException, ArabicShapingException;
    ByteArrayInputStream exportEtat( PDFListExistDTO pDFListExistDTO  )throws DocumentException, IOException, ArabicShapingException;
}
