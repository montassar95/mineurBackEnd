package com.cgpr.mineur.dto;

 
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RapportDetentionDTO {
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
    private Date  dateArrestation;
    private Date  dateDebutPeine;
    private Date dateLiberation;
   
    private String liberation;
    private String etablissementActuel;
  
    private String mouvementArrivee;
    private String mouvementSortie;
    
    private boolean mutationEnCours;
    private String age;
    private String nombreVisites;
    private boolean evasion;

    // Liste des affaires
    private List<Affaire> affaires;

    
    // Classe interne pour représenter une affaire
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Affaire {
    	private int numeroOrdinalAffaire;
        private String numeroAffaire;
        private String tribunal ;
        private String typeAffaire;
        private  String  accusations;
        private List<EvenementJuridique> evenementJuridiqueList;
        private String jugement;  
    }

    // Classe interne pour représenter un événement juridique dans une affaire
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EvenementJuridique {
        private String evenement;
        private Date date;
    }

 
}

