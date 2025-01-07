package com.cgpr.mineur.models;

import java.sql.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
@SuperBuilder
@Data
 
@NoArgsConstructor // Ajouter ce constructeur
@AllArgsConstructor 
@Entity
@Table(name = "chanLieu")
@DiscriminatorValue("ChangementLieu")
public class ChangementLieu extends Document { 
	
	private static final long serialVersionUID = 1L;
	 

	@ManyToOne
	@JoinColumn(name = "etablissementMutationFK")
	private Etablissement etablissementMutation;
	
	@ManyToOne
	@JoinColumn(name = "EtabChangeManiereFK")
	private EtabChangeManiere etabChangeManiere;
	
	
	private int jour;
	private int mois;
	private int annee;
	
	private String type;
	private String statut;

	 
}


