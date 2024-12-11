package com.cgpr.mineur.models;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "enf")
@JsonIgnoreProperties(ignoreUnknown = true) //Ignorer les Propriétés Inconnues : Si la propriété "img" n'est pas nécessaire dans la classe
public class Enfant  implements Serializable {
	@Id
	private String id;
	private String nom;
	private String prenom;

	private String nomPere;
	private String nomGrandPere;
	private String nomMere;
	private String prenomMere;

	@Column(name = "DATE_NAISSANCE")
	private LocalDate  dateNaissance;
	private String lieuNaissance;
	private String sexe;

	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "simplifier_criteria_id") // nom de la colonne de la référence
	private SimplifierCriteria simplifierCriteria;
	
	
 
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
	
//	@PrePersist
//    public void prePersist() {
//        if (dateNaissance != null) {
//        	 // Réinitialiser l'heure à 00:00:00
//		    Calendar calendar = Calendar.getInstance();
//		    calendar.setTime(dateNaissance); // dateNaissance est de type java.sql.Date
//
//		    // Réinitialiser l'heure à 00:00:00, et garantir que les millisecondes sont à zéro
//		    calendar.set(Calendar.HOUR_OF_DAY, 0);
//		    calendar.set(Calendar.MINUTE, 0);
//		    calendar.set(Calendar.SECOND, 0);
//		    calendar.set(Calendar.MILLISECOND, 0);
//
//		    // Recréer l'objet java.sql.Date sans l'heure
//		    dateNaissance = new java.sql.Date(calendar.getTimeInMillis());
//		    
//            }
//    }
}
