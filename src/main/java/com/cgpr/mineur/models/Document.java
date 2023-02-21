package com.cgpr.mineur.models;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.EmbeddedId;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Doc")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "document_type")
public  class Document implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected DocumentId documentId;

	protected String typeDocument;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "a_idEnf", referencedColumnName = "idEnf"),
			@JoinColumn(name = "a_numAff", referencedColumnName = "numAff"),
			@JoinColumn(name = "a_idTri", referencedColumnName = "idTri"),
			@JoinColumn(name = "a_numOrdArr", referencedColumnName = "numOrdArr") })

	protected Affaire affaire;
	
	private Date dateEmission;

	private Date dateDepotCarte;
	
	@ManyToOne
	@JoinColumn(name = "typFK")
	protected TypeAffaire typeAffaire;

	
	
	private String numArrestation;

	 

	@ManyToOne
	@JoinColumn(name = "etaFK")
	private Etablissement etablissement;
	
	
	@ManyToOne
	@JoinColumn(name = "perFK")
	private Personelle personelle;
	
	
	private Date dateInsertion;
 

	public Document(DocumentId documentId, String typeDocument) {
		super();
		this.documentId = documentId;
		this.typeDocument = typeDocument;
	}


	@Override
	public String toString() {
		return "Document [documentId=" + documentId + ", typeDocument=" + typeDocument + ", affaire=" + affaire
				+ ", dateEmission=" + dateEmission + ", dateDepotCarte=" + dateDepotCarte + ", typeAffaire="
				+ typeAffaire + ", numArrestation=" + numArrestation + ", etablissement=" + etablissement
				+ ", personelle=" + personelle + ", dateInsertion=" + dateInsertion + "]";
	}
	
	 
 
}