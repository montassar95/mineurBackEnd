package com.cgpr.mineur.dto;

import java.sql.Date;

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
public class AccusationCarteRecupDto {

                     
	private AccusationCarteRecupIdDto accusationCarteRecupId = new AccusationCarteRecupIdDto();
	
 
    private String textAccusation;

	private int jour;
	private int mois;
	private int annee;
	private int numOridinel;
	private int numOridinelLiee;
	private Date dateDebut;
	private Date dateFin;
	
	
	 
	private CarteRecupDto carteRecup;
 
	 
	private TitreAccusationDto titreAccusation;
	
}
