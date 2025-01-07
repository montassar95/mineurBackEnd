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
public class PrisonerPenaleDto {

	private String detenuId;
	private long numOrdinaleArrestation;
	private long numOrdinaleResidence;
	private String nom;
	private String prenom;

	private String nomPere;
	private String nomGrandPere;
	private String nomMere;
	private String prenomMere;
	private String lieuNaissance;
	private String sexe;
	private String numeroEcrou;
	private String nomEtablissement;
	
    private LocalDate  dateNaissance;
 
 	private String dateEntree;
 	private String adresse;
 	
 	
 	private String  debutPunition;
	private String  finPunition;
	
	private String  natureAffaire;
 
 	private int statut;
}
