package com.cgpr.mineur.resource;
 

 
import com.cgpr.mineur.models.ClassePenale;
import com.cgpr.mineur.models.Delegation;
import com.cgpr.mineur.models.Etablissement;
import com.cgpr.mineur.models.Gouvernorat;
import com.cgpr.mineur.models.Metier;
import com.cgpr.mineur.models.NiveauEducatif;
import com.cgpr.mineur.models.SituationFamiliale;
import com.cgpr.mineur.models.SituationSocial;
import com.cgpr.mineur.models.TypeAffaire;
import com.cgpr.mineur.models.TypeTribunal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PDFListExistDTO {

 private Etablissement etablissement;
 private String etatJuridiue;
  
 
 
 private ClassePenale classePenale;
 
 private NiveauEducatif niveauEducatif;
 
 private  Gouvernorat gouvernorat;

 private SituationFamiliale situationFamiliale;
 
 private SituationSocial situationSocial;
 
 private Metier metier;
 
 private Delegation delegation;
 

 
 private Gouvernorat gouvernoratTribunal;
 private TypeTribunal typeTribunal;
 
 private TypeAffaire typeAffaire;
 private int age1;
 private int age2;
 
 
 private String dateDebutGlobale;
 private String dateFinGlobale;
 
 
 private String checkEtranger;
 private String checkUniqueAff;
}
