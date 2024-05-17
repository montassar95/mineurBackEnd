package com.cgpr.mineur.dto;

import java.sql.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ArretProvisoireDto {

	 
	protected ArretProvisoireIdDto arretProvisoireId;

	
	private int jour;
	private int mois;
	private int annee;
	
	private Date dateDebut;
	private Date dateFin;
 
	private CarteRecupDto carteRecupDto;
	
	
	private  long daysDiff ;
	 
}
