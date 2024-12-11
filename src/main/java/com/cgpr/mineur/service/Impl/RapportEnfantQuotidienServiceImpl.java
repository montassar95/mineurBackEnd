package com.cgpr.mineur.service.Impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.dto.StatistiqueEtablissementDTO;
import com.cgpr.mineur.repository.RapportEnfantQuotidienRepository;
import com.cgpr.mineur.service.RapportEnfantQuotidienService;

 

 
 

@Service
public class RapportEnfantQuotidienServiceImpl  implements RapportEnfantQuotidienService{

	
	   @Autowired
	    private RapportEnfantQuotidienRepository rapportEnfantQuotidienRepository;
	   
	   
	   @Override
	    public List<StatistiqueEtablissementDTO> getStatistiquesParDate(String dateJson) {
		   LocalDate date = LocalDate.parse(dateJson);
	        return rapportEnfantQuotidienRepository.getStatistiquesParDate(  date);
	    }
 
}

