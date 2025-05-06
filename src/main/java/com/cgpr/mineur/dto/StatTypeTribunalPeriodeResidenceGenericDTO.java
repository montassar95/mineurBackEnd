package com.cgpr.mineur.dto;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatTypeTribunalPeriodeResidenceGenericDTO {
 
	private String typeTribunal;
	private String libelleEtablissement;
    private Long moins_3_mois;
    private Long entre_3_6_mois;
    private Long entre_6_9_mois;
    private Long entre_9_12_mois;
    private Long entre_12_15_mois;
    private Long entre_15_18_mois;
    private Long plus_18_mois;
    private Long total_typeTribunal;
}

