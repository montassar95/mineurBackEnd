package com.cgpr.mineur.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "etaChanMan")
public class EtabChangeManiere  implements Serializable {
	@Id
	private String id;

	 
	private String libelle_etabChangeManiere;

	@ManyToOne
	@JoinColumn(name = "code_gouv")
	private Gouvernorat gouvernorat;
 
}
