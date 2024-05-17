package com.cgpr.mineur.dto;

import java.sql.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResidenceDto {
	
	 
	private ResidenceIdDto residenceId;

	private String numArrestation;

	private Date dateEntree;
	private Date dateSortie;

	 
	private EtablissementDto etablissement;
	
 
	private EtablissementDto etablissementEntree;
	
	 
	private EtablissementDto etablissementSortie;

 
	private ArrestationDto arrestation;
	
	private int statut;
 
	private CauseMutationDto causeMutation;
	
	
 
	private CauseMutationDto causeMutationSortie;
	
	private String remarqueMutation;
	private int nombreEchappes;
	
 
	private Date dateFin;
	
	
	
 
	private EtabChangeManiereDto etabChangeManiere;



	public ResidenceDto(ResidenceIdDto residenceId, String numArrestation, int statut) {
		super();
		this.residenceId = residenceId;
		this.numArrestation = numArrestation;
		this.statut = statut;
	}



	public ResidenceDto(ResidenceIdDto residenceId, String numArrestation, EtablissementDto etablissement, int statut) {
		super();
		this.residenceId = residenceId;
		this.numArrestation = numArrestation;
		this.etablissement = etablissement;
		this.statut = statut;
	}
	
	
	
}
