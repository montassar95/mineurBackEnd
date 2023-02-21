package com.cgpr.mineur.models;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "del")
public class Delegation {
	@Id
	//@GeneratedValue
	private long id;
	 

	private String libelle_delegation;
 
	@ManyToOne
	@JoinColumn(name = "gouv")
	private Gouvernorat gouvernorat;

}
