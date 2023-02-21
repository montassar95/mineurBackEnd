package com.cgpr.mineur.models;

import java.sql.Date;

import javax.persistence.EmbeddedId;
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
@Table(name = "arrPro")
public class ArretProvisoire {

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
