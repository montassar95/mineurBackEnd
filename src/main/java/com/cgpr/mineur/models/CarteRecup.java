package com.cgpr.mineur.models;

import java.sql.Date;
import javax.persistence.DiscriminatorValue;
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
@Table(name = "carRec")
@DiscriminatorValue("CarteRecup")
public class CarteRecup extends Document {

	private static final long serialVersionUID = 1L;

	 

//	@Column(columnDefinition = "LONGTEXT")
	private String textJugement;

	 
 
	@ManyToOne
	@JoinColumn(name = "typJugFK")
	private TypeJuge typeJuge;

 
	
	private  long daysDiffJuge ;
	
	
	private int jour;
	private int mois;
	private int annee;
	
	
	
    private  long daysDiffArretProvisoire ;
	
	
	private int jourArretProvisoire;
	private int moisArretProvisoire;
	private int anneeArretProvisoire;
	
	 
	 
	
	
	private Date dateDebutPunition;

	private Date dateFinPunition;
	
	private String etatJuridiqueAvant;
 
}
