package com.cgpr.mineur.dto;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatistiqueEtablissementDTO {
	 private String id;
	 
    private String libelle_etablissement;
    
    private Long nbrStatutPenalJuge;
    
    private Long nbrStatutPenalArrete;
    
    private Long nbrStatutPenalLibre;
    
    private Long nbrNationaliteDifferentDeJuge1M;
    private Long nbrTypeAffaireId5M;
    
    private Long nbrNationaliteDifferentDeJuge1F;
    private Long nbrTypeAffaireId5F;
}
