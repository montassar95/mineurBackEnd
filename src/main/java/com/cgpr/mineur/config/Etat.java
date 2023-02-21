package com.cgpr.mineur.config;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.Enfant;
import com.cgpr.mineur.repository.DocumentRepository;
import com.cgpr.mineur.repository.EnfantRepository;

public class Etat {
	@Autowired
	private static EnfantRepository enfantRepository;
 
		@Autowired
		private static DocumentRepository documentRepository;

	
	public static String  etatJuridique( String id ) {
		 Optional<Enfant>  enfanttData = enfantRepository.findById(id);
			if (enfanttData.isPresent()) {
				List<Affaire> aData = documentRepository.findByArrestation(id );
	    		Affaire a = aData.stream()
	    		            .peek(num -> System.out.println("will filter " + num.getTypeDocument()))
	    								              .filter(x -> x.getTypeDocument().equals("CD")  || 
	    									            		   x.getTypeDocument().equals("CH")   ||
	    									              		   x.getTypeDocument().equals("T")   || 
	    									              		   x.getTypeDocument().equals("AP")   ||
	    									              	  	   x.getTypeDocument().equals("AE")   
	    								              	  )
	    		              .findFirst()
	    		              .orElse(null);
	    		System.out.println(a);
	    		if (a==null) {
	    			return "arrete";
	    		} else {
	    			return "juge";
	    		}
        }
			return null;
			
   }
	
}
