package com.cgpr.mineur.serviceReporting;

 

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import com.cgpr.mineur.resource.PDFListExistDTO;
import com.ibm.icu.text.ArabicShapingException;
import com.itextpdf.text.DocumentException;
import com.cgpr.mineur.dto.RapportDetentionDTO;
 

public interface GenererRapportPdfActuelService {

	 
   // List<Enfant> getEnfants(EnfantDTO e);
    
    
    
     ByteArrayInputStream genererRapportPdfActuel( PDFListExistDTO pDFListExistDTO  )throws DocumentException, IOException, ArabicShapingException;

     List<RapportDetentionDTO> genererRapportJsonActuel( PDFListExistDTO pDFListExistDTO);

}
