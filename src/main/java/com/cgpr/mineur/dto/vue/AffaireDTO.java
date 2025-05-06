package com.cgpr.mineur.dto.vue;

 

import lombok.Data;

import java.util.Date;

@Data
public class AffaireDTO {
    private String numeroAffaire;
    private String nomTribunal;
    private String typeAffaire;
    private Date dateDebutPunitionAffaire;
    private Date dateFinPunitionAffaire;
    private String typeDocumentDernier;
    private String typeDocumentDernierAlias;
    private Date dateEmissionDocumentDernier;
    private String typeDocumentAssocie;
    private String typeDocumentAssocieAlias;
    private Date dateEmissionDocumentAssocie;
    private String typeJuge;
    private Integer jourPunition;
    private Integer moisPunition;
    private Integer anneePunition;
    private String accusations;
}