package com.cgpr.mineur.dto;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatistiqueMouvementDTO {
	// private String id;
	 
    private String libelleEtablissement;
    
    private Long nombrePremiereEntree;
    
    private Long nombreLiberation;
    
    private Long nombreMutationEntrant;
    
    private Long nombreMutationSortant;
    private Long nombreChangementEtablissement;
   
}
