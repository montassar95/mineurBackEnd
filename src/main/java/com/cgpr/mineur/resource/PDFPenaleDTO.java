package com.cgpr.mineur.resource;
 

import com.cgpr.mineur.models.Arrestation;
import com.cgpr.mineur.models.Enfant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PDFPenaleDTO {

 private Enfant enfant;
 private Arrestation arrestation;
 private String numArrestation;
 private String centre;
 private String dateJugementPrincipale;
 private String dateDebut;
 private String dateFin;
 
 private String jourAge;
 private String moisAge;
 private String anneeAge;
 private String ageEnfant;
 
 private int jourPenal;

 private int moisPenal;
 private int  anneePenal;
 private int jourArret;
 private int moisArret;
 private int  anneeArret;
 private String accu;
 
 private int numTotaleEchappe;
 private int numTotaleRecidence;
 private int numTotaleRecidenceWithetabChangeManiere;
 
 private int nbrArrestation;
 private int nbrAffaires;
 
 
 private boolean appelParquet;
 private boolean appelEnfant;
 private boolean  ageAdulte;
 private String dateAppelParquet;
 private String dateAppelEnfant;
 
 private boolean sansDetail;
 private boolean sansImage;
  
 
}
