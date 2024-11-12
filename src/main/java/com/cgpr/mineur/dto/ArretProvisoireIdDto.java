package com.cgpr.mineur.dto;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class ArretProvisoireIdDto   {

	 

	 
	private String idEnfant;

	 
	private long numOrdinalArrestation;

	 
	private long numOrdinalAffaire;

	 
	private long numOrdinalzDoc;

 	 
 	private long numOrdinalDocByAffaire;
 	
 	 
	private long numOrdinalArret;

}