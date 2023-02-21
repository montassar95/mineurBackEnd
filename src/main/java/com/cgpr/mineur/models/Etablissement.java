package com.cgpr.mineur.models;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "eta")
public class Etablissement {
	@Id
	private String id;

	 
	private String libelle_etablissement;

	@ManyToOne
	@JoinColumn(name = "code_gouv")
	private Gouvernorat gouvernorat;

 @Column(columnDefinition = "integer default 0")
	private int statut;
}
