package com.cgpr.mineur.dto;

import java.sql.Date;

import com.cgpr.mineur.models.TypeJuge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


 
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarteRecupDto extends DocumentDto {

	 
	 

 
	private String textJugement;

	 
 
	 
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
