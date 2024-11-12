package com.cgpr.mineur.models;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "arrPro")
public class ArretProvisoire  implements Serializable {

	@EmbeddedId
	protected ArretProvisoireId arretProvisoireId;

	
	private int jour;
	private int mois;
	private int annee;
	
	private Date dateDebut;
	private Date dateFin;
	
	@ManyToOne
	private CarteRecup carteRecup;
	
	
	private  long daysDiff ;
	 
}
