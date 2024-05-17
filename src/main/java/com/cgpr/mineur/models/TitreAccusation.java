package com.cgpr.mineur.models;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Entity;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "titAcc")
public class TitreAccusation  implements Serializable {

	@Id
	private long id;

 

 
	private String titreAccusation;

 
	
	@ManyToOne
	@JoinColumn(name = "typAffFK")
	private TypeAffaire typeAffaire;
	
	
	 

}
