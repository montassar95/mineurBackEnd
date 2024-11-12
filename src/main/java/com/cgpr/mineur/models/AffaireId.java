
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
public class AffaireId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "idEnf")
	private String idEnfant;

	@Column(name = "numAff")
	private String numAffaire;

	@Column(name = "idTri")
	private long idTribunal;

	@Column(name = "numOrdArr")
	private long numOrdinaleArrestation;

}