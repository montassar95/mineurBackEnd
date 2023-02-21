package com.cgpr.mineur.models;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "claPen")
public class ClassePenale {
	@Id
	//@GeneratedValue
	private long id;
	 

	private String libelle_classe_penale;

}
