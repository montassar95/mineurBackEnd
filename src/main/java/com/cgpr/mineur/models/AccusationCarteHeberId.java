
package com.cgpr.mineur.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class AccusationCarteHeberId implements Serializable {

	private static final long serialVersionUID = 1L;


	@Column(name = "idEnf")
	private String idEnfant;

	@Column(name = "numOrdArr")
	private long numOrdinalArrestation;

	@Column(name = "numOrdAff")
	private long numOrdinalAffaire;

	@Column(name = "numOrdDoc")
	private long numOrdinalDoc;

	@Column(name = "numOrdDocByAff")
	private long numOrdinalDocByAffaire;
	
	
	@Column(name = "idTit")
	private long idTitreAccusation;


 
	
	
	
	
 
}