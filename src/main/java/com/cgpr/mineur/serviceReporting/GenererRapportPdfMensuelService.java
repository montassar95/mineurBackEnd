package com.cgpr.mineur.serviceReporting;

 

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.cgpr.mineur.resource.PDFListExistDTO;
import com.ibm.icu.text.ArabicShapingException;
import com.itextpdf.text.DocumentException;

 

public interface GenererRapportPdfMensuelService {

	 
 
    
    
    
    
	ByteArrayInputStream genererRapportPdfMensuel( PDFListExistDTO pDFListExistDTO)throws DocumentException, IOException, ArabicShapingException;
}
