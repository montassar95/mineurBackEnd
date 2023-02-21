package com.cgpr.mineur.models;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "tri")
public class Tribunal {

	@Id
	private long id;

	private String nom_tribunal;
	
	@ManyToOne
	@JoinColumn(name = "typTriFK")
	private TypeTribunal typeTribunal;
	
	
	@ManyToOne
	@JoinColumn(name = "gouFK")
	private Gouvernorat gouvernorat;
}
