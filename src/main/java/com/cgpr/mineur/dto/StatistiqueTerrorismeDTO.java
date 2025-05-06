package com.cgpr.mineur.dto;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatistiqueTerrorismeDTO {
	 
	 
    private String   etablissement;
    private String nationalite;
    private String typeAffaire;
    private String situationJudiciaire;
    private Long totale;
    
    
   
}
