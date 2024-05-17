package com.cgpr.mineur.models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import javax.persistence.Entity;

@Data
@Entity
@Table(name = "chanLieu")
@DiscriminatorValue("ChangementLieu")
public class ChangementLieu extends Document { 
	
	private static final long serialVersionUID = 1L;
	 

	@ManyToOne
	@JoinColumn(name = "etablissementMutationFK")
	private Etablissement etablissementtMutation;
	
	@ManyToOne
	@JoinColumn(name = "EtabChangeManiereFK")
	private EtabChangeManiere etabChangeManiere;
	
	
	private int jour;
	private int mois;
	private int annee;
	
	private String type;

}


