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
	
	
	
	 // Constructeur spécifique pour detentionId et affaires
    public RapportDetentionDTO(String detentionId ) {
        this.detentionId = detentionId;
        
    }

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
    private Date   dateFinPeine;
   
    private String liberation;
    private String etablissementActuel;
  
    private String mouvementArrive;
    private String mouvementSortie;
    
    private boolean mutationEnCours;
    private int age;
    private String nombreVisites;
    private boolean evasion;

    // Liste des affaires
    private List<RapportAffaire> affaires ;

    
    // Classe interne pour représenter une affaire
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RapportAffaire {
    	 // Constructeur spécifique pour numeroOrdinalAffaire
        public RapportAffaire(int numeroOrdinalAffaire) {
            this.numeroOrdinalAffaire = numeroOrdinalAffaire;
        }
    	private int numeroOrdinalAffaire;
        private String numeroAffaire;
        private String tribunal ;
        private String typeAffaire;
        private  String  accusations;
        private List<RapportEvenementJuridiqueDto> evenementJuridiqueList;
        private String jugement;  
    }

    // Classe interne pour représenter un événement juridique dans une affaire
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RapportEvenementJuridiqueDto {
        private String evenement;
        private Date date;
    }

 
}

