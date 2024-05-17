package com.cgpr.mineur.models;
import java.io.Serializable;

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
public class TypeAffaire  implements Serializable {
	@Id
	private long id;

	private String libelle_typeAffaire;
	private int statutException;
	private int statutNiveau;

}
