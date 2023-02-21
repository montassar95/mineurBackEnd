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
@Table(name = "resTran")
public class ResultatTransfert {
	@Id
	//@GeneratedValue
	private long id;
	 

	private String libelle_resultat;

}
 
