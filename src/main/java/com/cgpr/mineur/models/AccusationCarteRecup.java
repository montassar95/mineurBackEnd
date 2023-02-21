package com.cgpr.mineur.models;

import java.sql.Date;

import javax.persistence.EmbeddedId;
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
@Table(name = "accCarRec")
public class AccusationCarteRecup {

	@EmbeddedId                        
	private AccusationCarteRecupId accusationCarteRecupId = new AccusationCarteRecupId();;
	
// 	@Column(columnDefinition = "LONGTEXT")
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
	
}
