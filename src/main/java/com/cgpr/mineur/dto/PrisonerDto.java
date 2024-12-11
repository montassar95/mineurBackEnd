package com.cgpr.mineur.dto;

import javax.persistence.Entity;

 

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
 
public class PrisonerDto {

	private String idPrisoner;
	private String firstName;
	private String  lastName ;
	private String fatherName;
	private String  grandFatherName;
	private String codeNationalite;
	
	
	private String codeGouvernorat;
	private String codePrison;
	private String  namePrison ;
	private String  codeResidance;
	private String  anneeResidance;
	private String  statutResidance;
	private String  numDetention;
	private String eventDate;
	
	private String  etat;
	private String  type;
	
	private String  dateDebutPunition;
	private String  dateFinPunition;
		
 

	       
}
