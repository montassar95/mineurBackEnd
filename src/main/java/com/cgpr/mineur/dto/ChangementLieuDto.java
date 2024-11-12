package com.cgpr.mineur.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ChangementLieuDto extends DocumentDto { 
	
  

	private EtablissementDto etablissementMutation;
	
	 	private EtabChangeManiereDto etabChangeManiere;
	
	
	private int jour;
	private int mois;
	private int annee;
	
	private String type;

}


