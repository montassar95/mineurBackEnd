package com.cgpr.mineur.models;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "res")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Residence implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ResidenceId residenceId;

	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "ar_idEnfant", referencedColumnName = "idEnf"),
			@JoinColumn(name = "ar_numOrdinale", referencedColumnName = "numOrd") })
	private Arrestation arrestation;
	
	private String numArrestation;
	private int statut;
	
	private Date datePassage;
	private Date dateEntree;
	private Date dateSortie;

	@ManyToOne
	@JoinColumn(name = "etaFK")
	private Etablissement etablissement;
	
	@ManyToOne
	@JoinColumn(name = "etaPassageFK")
	private Etablissement etablissementPassage;
	
	@ManyToOne
	@JoinColumn(name = "etaFKE")
	private Etablissement etablissementEntree;
	
	@ManyToOne
	@JoinColumn(name = "etaFKS")
	private Etablissement etablissementSortie;


	
	
	@ManyToOne
	@JoinColumn(name = "cauMutFK")
	private CauseMutation causeMutation;
	
	
	@ManyToOne
	@JoinColumn(name = "cauMutFKS")
	private CauseMutation causeMutationSortie;
	
	private String remarqueMutation;
	private int nombreEchappes;
	
 	@OneToMany(mappedBy = "residenceEchapper")
     private List<Echappes> echappes; // Échappements liés à cette résidence
	
	
	
	
	
	@ManyToOne
	@JoinColumn(name = "etabChange")
	private EtabChangeManiere etabChangeManiere;

	@ManyToOne
	@JoinColumn(name = "etabChangeEntree")
	private EtabChangeManiere etabChangeManiereEntree;

	
	@Transient
	private Date dateFin;
	
	
	@Transient
	private String  nbVisite ;
	   
	   
	public Residence(ResidenceId residenceId, String numArrestation, int statut) {
		super();
		this.residenceId = residenceId;
		this.numArrestation = numArrestation;
		this.statut = statut;
	}



	public Residence(ResidenceId residenceId, String numArrestation, Etablissement etablissement, int statut) {
		super();
		this.residenceId = residenceId;
		this.numArrestation = numArrestation;
		this.etablissement = etablissement;
		this.statut = statut;
	}
	
	
	
}
