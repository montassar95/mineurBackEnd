package com.cgpr.mineur.models;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "aff")
public class Affaire  implements Serializable {

	@EmbeddedId
	private AffaireId affaireId;

	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "a_idEnf", referencedColumnName = "idEnf"),
			      @JoinColumn(name = "a_numOrd", referencedColumnName = "numOrd") })
	private Arrestation arrestation;

	private long numOrdinalAffaire;

	private long numOrdinalAffaireByAffaire;

	@ManyToOne
	@JoinColumn(name = "tribunalFK")
	private Tribunal tribunal;

	@OneToOne
	@JoinColumns({ @JoinColumn(name = "aL_idEnf", referencedColumnName = "idEnf"),
			@JoinColumn(name = "aL_numAff", referencedColumnName = "numAff"),
			@JoinColumn(name = "aL_idTri", referencedColumnName = "idTri"),
			@JoinColumn(name = "aL_numOrdArr", referencedColumnName = "numOrdArr")

	})
	private Affaire affaireLien;

	@OneToOne
	@JoinColumns({ @JoinColumn(name = "aF_idEnf", referencedColumnName = "idEnf"),
			@JoinColumn(name = "aF_numAff", referencedColumnName = "numAff"),
			@JoinColumn(name = "aF_idTri", referencedColumnName = "idTri"),
			@JoinColumn(name = "aF_numOrdArr", referencedColumnName = "numOrdArr")

	})
	private Affaire affaireAffecter;

	private int statut;

	
	
	@JsonIgnore
	@OneToMany(mappedBy = "affaire", cascade = CascadeType.REMOVE)
	private List<Document> documents;

	@Transient
	private List<TitreAccusation> titreAccusations;

	private String typeDocument;

	private String typeDocumentActuelle;

	private Date dateEmissionDocument;

	@ManyToOne
	@JoinColumn(name = "typeAffaireFK3")
	private TypeAffaire typeAffaire;

	@Transient
	private int jour;
	@Transient
	private int mois;
	@Transient
	private int annee;

	@Transient
	private int jourArret;
	@Transient
	private int moisArret;
	@Transient
	private int anneeArret;

	@Transient
	private String typeFile;

	@ManyToOne
	@JoinColumn(name = "typeJugeFK")
	private TypeJuge typeJuge;

	@Transient
	private Date dateEmission;

	private Date dateDebutPunition;

	private Date dateFinPunition;
	private long daysDiffJuge;

	@Transient
	private boolean affairePrincipale;

}
