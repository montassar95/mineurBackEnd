package com.cgpr.mineur.dto;

import java.sql.Date;
import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.cgpr.mineur.models.Affaire;
import com.cgpr.mineur.models.ArretProvisoire;
import com.cgpr.mineur.models.Liberation;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.models.Tribunal;
import com.cgpr.mineur.models.TypeAffaire;

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
public class CalculeAffaireDto {

 
	 

	 
	private int  jourPenal ;
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
	
	
	private boolean sansAffaire;
	private int nbrAffaires;
	private boolean displayArrestation;
	//private boolean displayAffaire;
	
	
	private boolean isChangementLieuMu;
	private boolean isChangementLieuCh;
	
	private boolean  isAgeAdulte;
	
	private String etatJuridique;
	
	
//    private String numAffairePricipale;
//	
//	
//	
//	private Tribunal tribunalPricipale;
//	
//	private long numOrdinalAffairePricipale;
//	
//	
//	private TypeAffaire typeAffairePricipale;
	
  	private List<Affaire> affaires ;
  	private Liberation liberation;
  	
  	private int totaleRecidenceWithetabChangeManiere;
  	
  	private int totaleRecidence;
  	
  	private int totaleEchappe;
   
  	List<Residence> residences ;
  	
  	
  	private String dateDebut;
  	private String dateFin;

  	
  	 List<ArretProvisoire> arretProvisoires ;
  	 
}
