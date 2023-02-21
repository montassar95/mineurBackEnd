package com.cgpr.mineur.models;

import java.sql.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "res")
public class Residence {
	
	@EmbeddedId
	private ResidenceId residenceId;

	private String numArrestation;

	private Date dateEntree;
	private Date dateSortie;

	@ManyToOne
	@JoinColumn(name = "etaFK")
	private Etablissement etablissement;
	
	@ManyToOne
	@JoinColumn(name = "etaFKE")
	private Etablissement etablissementEntree;
	
	@ManyToOne
	@JoinColumn(name = "etaFKS")
	private Etablissement etablissementSortie;

	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "ar_idEnfant", referencedColumnName = "idEnf"),
			@JoinColumn(name = "ar_numOrdinale", referencedColumnName = "numOrd") })
	private Arrestation arrestation;
	
	private int statut;
	@ManyToOne
	@JoinColumn(name = "cauMutFK")
	private CauseMutation causeMutation;
	
	
	@ManyToOne
	@JoinColumn(name = "cauMutFKS")
	private CauseMutation causeMutationSortie;
	
	private String remarqueMutation;
	private int nombreEchappes;
	
	@Transient
	private Date dateFin;
	
	
	
	@ManyToOne
	@JoinColumn(name = "etabChange")
	private EtabChangeManiere etabChangeManiere;
}
