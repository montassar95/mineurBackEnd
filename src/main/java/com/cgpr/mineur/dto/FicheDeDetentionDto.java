package com.cgpr.mineur.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FicheDeDetentionDto {

 
	 

	 
	private int   jourPenal ;
	private int   moisPenal;
	private int   anneePenal;

	private int  jourArret;
	private int   moisArret;
	private int  anneeArret;

	private String   dateJugementPrincipale ;
	
	private String dateAppelParquet ;
	private String  dateAppelEnfant ;
	private boolean  isAppelParquet ;
	private boolean  isAppelEnfant ;
	private boolean  isDateJuge ;
	
	
 
	 
	 
	 
	
 	private boolean isChangementLieuMu;
	private boolean isChangementLieuCh;
	
	private boolean  isAgeAdulte;
	
	private String etatJuridique;
	
	
 
	
	private ArrestationDto arrestation;
  	private LiberationDto liberation;
  	
  
   
  	private String dateDebut;
  	private String dateFin;
  	
  	private List<AffaireDto> affaires ;
  	List<ResidenceDto> residences ;
  	
  	private List<ArretProvisoireDto> arretProvisoires ;
  	
	private int totaleRecidenceWithetabChangeManiere;
  	
  	private int totaleRecidence;
  	
  	private int totaleEchappe;
  	 
}
