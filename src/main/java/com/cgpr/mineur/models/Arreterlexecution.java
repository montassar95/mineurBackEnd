package com.cgpr.mineur.models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
@SuperBuilder
 @NoArgsConstructor // Ajouter ce constructeur
 @AllArgsConstructor 
@Data
 
@Entity
@Table(name = "arrLex")
@DiscriminatorValue("Arreterlexecution")
public class Arreterlexecution extends Document { 
	
	private static final long serialVersionUID = 1L;
	 
	private String typeFile;
	@ManyToOne
	@JoinColumn(name = "motArrlexFK")
	private MotifArreterlexecution motifArreterlexecution;
	

}


