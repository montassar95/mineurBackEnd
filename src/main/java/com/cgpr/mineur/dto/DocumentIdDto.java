package com.cgpr.mineur.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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
public class DocumentIdDto  {

	 

 
	private String idEnfant;

 
	private long numOrdinalArrestation;

 
	private long numOrdinalAffaire;

 
	private long numOrdinalDoc;

 
 	private long numOrdinalDocByAffaire;
 	
 	
 

}