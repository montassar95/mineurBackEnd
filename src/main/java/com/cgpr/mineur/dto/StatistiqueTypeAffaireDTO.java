package com.cgpr.mineur.dto;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatistiqueTypeAffaireDTO {
	// private String id;
	 
    private String typeAffaire;
    private long arrete;
    private long juge;
    private long max_statut;
    
     
   
}
