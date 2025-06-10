package com.cgpr.mineur.dto;

import java.sql.Date;
import java.util.List;

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
public class PenalAffaireDTO {
	
	
	    private String tnumseqaff;
	    private String libelleNature;
	    private String libelleTribunal;
	    private String tnumjafFormatte;
	    private String accusationsConcatenees;
	    private String etatAffaire;
	    private String typeMandat;
	    private String tdatdep;
	    private Integer totalAnnees;
	    private Integer totalMois;
	    private Integer totalJours;

	    private String libelleJugement;
	    private String numeroEcrou;
	    private String dateDebutMin;
	    private String dateFinMax;

	    private String typeDocument;
	    private String tcodsit;
	    private String typeJugement;
	    private String natureJugement;
	    private String natureTribunal;
	    private String typeAffaire;
	    private String textJugement;
	    
	    
	    private int rowAffairePrincipale ;
	    private int rowOrder ;
	    
	   
}
