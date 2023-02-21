package com.cgpr.mineur.models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "rev")
@DiscriminatorValue("Revue")
public class Revue extends Document {

	private static final long serialVersionUID = 1L;
	
//	@Column(columnDefinition = "LONGTEXT")
	private String textJugement;

//	private String refuse;

	 

}
