package com.cgpr.mineur;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConstructorBinding;

import com.cgpr.mineur.repository.ResidenceRepository;
import com.cgpr.mineur.repository.TitreAccusationRepository;
import com.cgpr.mineur.service.TitreAccusationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;

public class test {
	
	private static Date getFirstDayOfMonth() {
//	    Calendar calendar = Calendar.getInstance();
//	    calendar.set(Calendar.DAY_OF_MONTH, 1); //c'est pour avril seulemrnt 4
//	    return calendar.getTime();
	    Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1); // C'est pour le mois en cours
        calendar.set(Calendar.MONTH, Calendar.APRIL); // Choisir le mois d'avril
        calendar.set(Calendar.YEAR, 2024); // Choisir l'année
        return calendar.getTime();
	}
	  public static void main(String[] args) {
		  Date premierJourDuMois = getFirstDayOfMonth();
	        System.err.println(premierJourDuMois.toString() );
	        
	    }
	  
	  
	  
	 

}
 

