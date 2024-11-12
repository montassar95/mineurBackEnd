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
@Data
@Entity
@NoArgsConstructor // Ajouter ce constructeur
@AllArgsConstructor 
@Table(name = "tra")
@DiscriminatorValue("Transfert")
public class Transfert extends Document {

	private static final long serialVersionUID = 1L;

 
	
	private String typeFile;
	@ManyToOne
	@JoinColumn(name = "resTranFK")
	private ResultatTransfert resultatTransfert;
	 

}
