package com.cgpr.mineur.dto;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
public class TypeAffaireDto {
 
	private long id;

	private String libelle_typeAffaire;
	private int statutException;
	private int statutNiveau;

}
