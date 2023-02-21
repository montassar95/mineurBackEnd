package com.cgpr.mineur.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class ResidenceId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "idEnf")
	private String idEnfant;

	@Column(name = "numOrdArr")
	private long numOrdinaleArrestation;
	
	@Column(name = "numOrdRes")
	private long numOrdinaleResidence;

}