package com.cgpr.mineur.models;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "residence_juge_view")
public class ResidenceJugeView {
	
	@EmbeddedId
	private ResidenceId residenceId;

	private String numArrestation;

//	private Date dateEntree;
//	private Date dateSortie;

	@Column(name = "etaFK")
 	private String etaFK;
//	
//	 
//	private Etablissement etablissementEntree;
//	
//	 
//	private Etablissement etablissementSortie;

	 
	//private Arrestation arrestation;
	
	private int statut;
	 
	//private CauseMutation causeMutation;
	
	
	 
	//private CauseMutation causeMutationSortie;
	
	//private String remarqueMutation;
	//private int nombreEchappes;
	
//	@Transient
//	private Date dateFin;
	
	
	
	 
	//private EtabChangeManiere etabChangeManiere;
}
