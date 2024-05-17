package com.cgpr.mineur.models;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "enf")
public class Enfant  implements Serializable {
	@Id
	private String id;
	private String nom;
	private String prenom;

	private String nomPere;
	private String nomGrandPere;
	private String nomMere;
	private String prenomMere;

	private Date dateNaissance;
	private String lieuNaissance;
	private String sexe;

 
 @Transient 
	@Lob
	private String img;

	@ManyToOne
	private Nationalite nationalite;

	@ManyToOne
	private NiveauEducatif niveauEducatif;

	@ManyToOne
	private SituationFamiliale situationFamiliale;

	private int nombreFreres;

	@ManyToOne
	private Gouvernorat gouvernorat;

	@ManyToOne
	private Delegation delegation;

	private String adresse;

	private String surnom;

	private String alias;

	@ManyToOne
	private ClassePenale classePenale;
	
	@ManyToOne
	private SituationSocial situationSocial;
	
	@ManyToOne
	private Metier metier;
	
 
	
	@Transient
	private String etat;
	
	private int nbrEnfant;
	
	
}
