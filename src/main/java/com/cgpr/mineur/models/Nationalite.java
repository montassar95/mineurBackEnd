package com.cgpr.mineur.models;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Entity;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "nat")
public class Nationalite  implements Serializable {
	@Id
	private long id;
	 
	private String libelle_nationalite;

}
