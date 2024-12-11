package com.cgpr.mineur.dto;

import java.sql.Date;
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
	
//	public SearchDetenuDto(
//					String detenuId,
//					String nom,
//		            String prenom,
//		            String nomPere, 
//		            String nomGrandPere, 
//		            String nomMere, 
//		            String prenomMere,
//		            LocalDate dateNaissance, 
//		            String lieuNaissance, 
//		            String sexe,
//		            String numeroEcrou,
//		            String nomEtablissement, 
//		            Date dateEntree,
//		            int statut) {
//			this.detenuId = detenuId;
//			this.nom = nom;
//			this.prenom = prenom;
//			this.nomPere = nomPere;
//			this.nomGrandPere = nomGrandPere;
//			this.nomMere = nomMere;
//			this.prenomMere = prenomMere;
//			this.dateNaissance = dateNaissance;
//			this.lieuNaissance = lieuNaissance;
//			this.sexe = sexe;
//			this.numeroEcrou = numeroEcrou;
//			this.nomEtablissement = nomEtablissement;
//			this.dateEntree = dateEntree;
//			this.statut = statut;
//}

}
