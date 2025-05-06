package com.cgpr.mineur.dto;



import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatistiqueGlobaleDTO {
	  private String libelleEtablissement;;
	    private Map<String, Integer> statistics;

	    
	}
 
