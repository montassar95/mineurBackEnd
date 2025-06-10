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
public class SearchDetenuDto {

	
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
 	private int statut;
 	
 
// 	public SearchDetenuDto(
// 		    String detenuId,
// 		    long numOrdinaleArrestation,
// 		    long numOrdinaleResidence,
// 		    String nom,
// 		    String prenom,
// 		    String nomPere,
// 		    String nomGrandPere,
// 		    String nomMere,
// 		    String prenomMere,
// 		    String lieuNaissance,
// 		    String sexe,
// 		    String numeroEcrou,
// 		    String nomEtablissement,
// 		    LocalDate dateNaissance,
// 		    String dateEntree,
// 		    int statut
// 		) {
// 		    this.detenuId = detenuId;
// 		    this.numOrdinaleArrestation = String.valueOf(numOrdinaleArrestation);
// 		    this.numOrdinaleResidence = String.valueOf(numOrdinaleResidence);
// 		    this.nom = nom;
// 		    this.prenom = prenom;
// 		    this.nomPere = nomPere;
// 		    this.nomGrandPere = nomGrandPere;
// 		    this.nomMere = nomMere;
// 		    this.prenomMere = prenomMere;
// 		    this.lieuNaissance = lieuNaissance;
// 		    this.sexe = sexe;
// 		    this.numeroEcrou = numeroEcrou;
// 		    this.nomEtablissement = nomEtablissement;
// 		    this.dateNaissance = dateNaissance;
// 		    this.dateEntree = dateEntree;
// 		    this.statut = statut;
// 		}

}
