package com.cgpr.mineur.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

 
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChangementLieuDto extends DocumentDto { 
	
  

	private EtablissementDto etablissementtMutation;
	
	 	private EtabChangeManiereDto etabChangeManiere;
	
	
	private int jour;
	private int mois;
	private int annee;
	
	private String type;

}

