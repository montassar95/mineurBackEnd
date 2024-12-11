package com.cgpr.mineur.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EnfantDto {
	 
	private String id;
	private String nom;
	private String prenom;

	private String nomPere;
	private String nomGrandPere;
	private String nomMere;
	private String prenomMere;

	private LocalDate  dateNaissance;
	private String lieuNaissance;
	private String sexe;

 
 
	private String img;

	 
	private NationaliteDto nationalite;

	 
	private NiveauEducatifDto niveauEducatif;

	 
	private SituationFamilialeDto situationFamiliale;

	private int nombreFreres;

	 
	private GouvernoratDto gouvernorat;

	 
	private DelegationDto delegation;

	private String adresse;

	private String surnom;

	private String alias;

	 
	private ClassePenaleDto classePenale;
	
	 
	private SituationSocialDto situationSocial;
	
	 
	private MetierDto metier;
	
 
	
	 
	private String etat;
	
	private Integer nbrEnfant;
	
	
}
