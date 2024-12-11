package com.cgpr.mineur.models;

 

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class RapportEnfantQuotidien  implements Serializable {
	

	@EmbeddedId
	private RapportEnfantQuotidienId rapportEnfantQuotidienId;
	 
	 
	
	
	private String statutPenal;
	
	
	@ManyToOne
	@JoinColumn(name = "etaFK")
	private Etablissement etablissement;
	
	
	@ManyToOne
	@JoinColumn(name = "natFK")
	private Nationalite  nationalite;
	
	@ManyToOne
	@JoinColumn(name = "typeAffFK")
	private TypeAffaire typeAffaire;
	
	private String  sexe ;
	
	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "a_idEnf", referencedColumnName = "idEnf"),
			      @JoinColumn(name = "a_numOrd", referencedColumnName = "numOrd") })
	private Arrestation arrestation;
	
	@OneToOne
	@JoinColumns({ 
		@JoinColumn(name = "l_idEnf", referencedColumnName = "idEnf"),
		@JoinColumn(name = "l_numOrd", referencedColumnName = "numOrd")
			 
	})
	private Liberation liberation;
	
	@Lob
	private String residance;
}
 
