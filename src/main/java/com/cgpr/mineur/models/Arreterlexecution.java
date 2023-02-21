package com.cgpr.mineur.models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import javax.persistence.Entity;
//@NoArgsConstructor
//@AllArgsConstructor
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


