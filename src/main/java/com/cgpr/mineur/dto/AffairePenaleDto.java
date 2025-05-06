package com.cgpr.mineur.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AffairePenaleDto {

	private String tnumseqaff;
    private String libelleTribunal;
    private String tnumjafFormatte;
    private String etatAffaire;
    private String libelleNature;
    private String numeroEcrou;
    private String typeMandat;
    private String tntypema;
 	
 	
}
