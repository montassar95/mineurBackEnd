package com.cgpr.mineur.dto;

import javax.persistence.Id;

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
public class FonctionDto {
	 
	private long id;
	 

	private String libelle_fonction;

}
