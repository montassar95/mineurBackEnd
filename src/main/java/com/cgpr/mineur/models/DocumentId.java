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
public class DocumentId implements Serializable {

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
 	
 	
// 	@ManyToOne(fetch=FetchType.LAZY)
// 	@JoinColumns({
// 	  @JoinColumn(name = "codProvincia", insertable = false, updatable = false),
// 	  @JoinColumn(name = "codRegion", insertable = false, updatable = false)
// 	})

}