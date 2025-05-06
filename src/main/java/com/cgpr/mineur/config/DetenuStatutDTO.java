package com.cgpr.mineur.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

 

import lombok.Data;

@Data
public class DetenuStatutDTO {
	private String detentionId;
    private String nomComplet; 
   
    private String detailsNaissance;  
   
    private String nationalite;
    private String niveauDeFormationEtProfession;  
    private String situationFamilialeEtSociale;  
    private String adresse;
    private String classePenale;

    private String situationJudiciaire;
  
    private String numeroEcrou;
    private String  dateArrestation;
    private Date  dateDebutPeine;
    private Date   dateFinPeine;
   
     
    private String etablissementActuel;

    private String evenementEntree ;
    private String age;
    private String nbrVisite;
  
 // Liste des affaires
    private List<AffaireDTO> affaires;
    
 // Constructeur
    public DetenuStatutDTO() {
        this.affaires = new ArrayList<>();
    }
    
 // Ajouter une affaire
    public void addAffaire( String numeroAffaire, 
				    		String tribunal,
				    		String typeAffaire ,
				    		String accusations   , 
				    		String documentAssocie ,String  documentDernier,
				    		String dateDebutPunitionAffaire ,
				    		String dateFinPunitionAffaire,
				    		String punition
				    		) {
        affaires.add(new AffaireDTO(numeroAffaire, tribunal, typeAffaire , accusations ,  documentAssocie ,   documentDernier,  dateDebutPunitionAffaire , dateFinPunitionAffaire , punition));
    }
    
}
