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
@Table(name = "del")
public class Delegation  implements Serializable {
	@Id
	private long id;
	 

	private String libelle_delegation;
 
	@ManyToOne
	@JoinColumn(name = "gouv")
	private Gouvernorat gouvernorat;

}
