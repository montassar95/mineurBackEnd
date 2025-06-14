package com.cgpr.mineur.models;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "accCarRec")
public class AccusationCarteRecup  implements Serializable {

	@EmbeddedId                        
	private AccusationCarteRecupId accusationCarteRecupId = new AccusationCarteRecupId();;
	
 
    private String textAccusation;

	private int jour;
	private int mois;
	private int annee;
	private int numOridinel;
	private int numOridinelLiee;
	private Date dateDebut;
	private Date dateFin;
	
	
	@ManyToOne
	private CarteRecup carteRecup;
 
	@ManyToOne
	@JoinColumn(name = "TitreAccusationFK")
	private TitreAccusation titreAccusation;
	
//	@Column(name = "type", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'jugement'")
//	private String type;
	
}
