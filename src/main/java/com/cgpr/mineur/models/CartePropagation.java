package com.cgpr.mineur.models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
 
@Entity
@Table(name = "carPropa")
@DiscriminatorValue("CartePropa")
public class CartePropagation extends Document {

	private static final long serialVersionUID = 1L;

	 

	private int jour;
	private int mois;
	private int annee;

  
}
