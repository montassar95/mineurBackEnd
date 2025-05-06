package com.cgpr.mineur.config;

import java.util.Date;

import lombok.Data;

@Data
public class AffaireDTO {
    private String numeroAffaire;
    private String tribunal;
    private String typeAffaire;
    private String accusations;
    private String documentAssocie;
    private String documentDernier;
    private String debutPunitionAffaire;
    private String finPunitionAffaire;
    private String  punition;
    public AffaireDTO(String numeroAffaire, String tribunal, String typeAffaire , 
    		String accusations , String documentAssocie ,String  documentDernier ,
    		String debutPunitionAffaire , String finPunitionAffaire ,String punition) {
        this.numeroAffaire = numeroAffaire;
        this.tribunal = tribunal;
        this.typeAffaire = typeAffaire;
        this.accusations = accusations;
        this.documentAssocie = documentAssocie;
        this.documentDernier = documentDernier;
        this.debutPunitionAffaire = debutPunitionAffaire;
        this.finPunitionAffaire = finPunitionAffaire;
        this.punition = punition;
    }
}

