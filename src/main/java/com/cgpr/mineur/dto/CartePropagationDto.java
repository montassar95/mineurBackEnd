package com.cgpr.mineur.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
 
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class CartePropagationDto extends DocumentDto {
  

	private int jour;
	private int mois;
	private int annee;
	public CartePropagationDto(DocumentIdDto documentId, String typeDocument, int jour, int mois, int annee) {
		super(documentId, typeDocument);
		this.jour = jour;
		this.mois = mois;
		this.annee = annee;
	}
	
	

  
}
