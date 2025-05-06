package com.cgpr.mineur.dto;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatTypeTribunalPeriodeDTO {
 
	private String typeTribunal;
    private Long moins_3_mois;
    private Long entre_3_6_mois;
    private Long entre_6_9_mois;
    private Long entre_9_12_mois;
    private Long plus_12_mois;
    private Long total_typeTribunal;
}

