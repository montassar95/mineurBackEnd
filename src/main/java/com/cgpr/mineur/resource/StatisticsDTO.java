package com.cgpr.mineur.resource;
 

import java.util.List;
import java.util.Map;

import com.cgpr.mineur.models.Arrestation;
import com.cgpr.mineur.models.Enfant;
import com.cgpr.mineur.models.Residence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StatisticsDTO {
 
	
private int nbrAll;
private int nbrEtrange;

 private int nbrArret;
 private int nbrJuge;
 

  
 private int nbrEntreeMutation;
 private int nbrSortieMutation;
 
 private int nbrEntreeArr;
 private int nbrSortieArr;
 
 private int nbrDebutant;
 private int nbrAncien;
 
 private int nbrAge13;
 private int nbrAge14;
 private int nbrAge15;
 private int nbrAge16;
 private int nbrAge17;
 private int nbrAge18;
 
 
 private int nbrIgnorant;
 private int nbrPrimaire;
 private int nbrPrepa;
 private int nbrSecondaire;
 private int nbrFormation;
 private int nbrEtudiant;
 
 
 
 private int nbrSiFaAvec;
 private int nbrSiFaParentSepa;
 private int nbrSiFaOrphelinPe;
 private int nbrSiFaOrphelinMe;
 private int nbrSiFaOrphelinPeMe;
 private int nbrSiFaCasSoci;
 
 private Map<String, Integer> typeAffairesArrete   ;
 private Map<String, Integer> typeAffairesJuge   ;
 
 
 
 
}
