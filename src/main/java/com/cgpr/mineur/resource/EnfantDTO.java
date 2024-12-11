package com.cgpr.mineur.resource;
 

 

import java.sql.Date;
import java.time.LocalDate;

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


 	private LocalDate  dateNaissance;
 
	private String sexe;
	
	
	private String nomMere;
	private String prenomMere;

}
