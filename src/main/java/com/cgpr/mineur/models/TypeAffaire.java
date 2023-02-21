package com.cgpr.mineur.models;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "typAff")
public class TypeAffaire {
	@Id
	//@GeneratedValue
	private long id;
	//private long code_typeAffaire;
	private String libelle_typeAffaire;
	private int statutException;
	private int statutNiveau;

}
