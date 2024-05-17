package com.cgpr.mineur.serviceReporting;

 

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

 

public interface EnfantAllCentreService {

	 
   // List<Enfant> getEnfants(EnfantDTO e);
    
    
    
    
    byte[] exportEtatAllCentre( String dateString)throws DocumentException, IOException, ArabicShapingException;
}
