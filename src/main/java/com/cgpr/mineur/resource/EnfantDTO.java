package com.cgpr.mineur.resource;
 

 

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EnfantDTO {

	private String nom;
	private String prenom;

	private String nomPere;
	private String nomGrandPere;
	private String nomMere;
	private String prenomMere;

 	private Date dateNaissance;
 
	private String sexe;

}
