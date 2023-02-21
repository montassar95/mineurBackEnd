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
public class ArretProvisoireId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "idEnf")
	private String idEnfant;

	@Column(name = "numOrdArr")
	private long numOrdinalArrestation;

	@Column(name = "numOrdAff")
	private long numOrdinalAffaire;

	@Column(name = "numOrdDoc")
	private long numOrdinalzDoc;

 	@Column(name = "numOrdDocByAff")
 	private long numOrdinalDocByAffaire;
 	
 	@Column(name = "numOrdArt")
	private long numOrdinalArret;

}