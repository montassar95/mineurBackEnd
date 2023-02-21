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
@Table(name = "typJug")
public class TypeJuge {
	@Id
	//@GeneratedValue
	private long id;
	//private long code_typeJuge;
	private String libelle_typeJuge;
	private String situation;

}
