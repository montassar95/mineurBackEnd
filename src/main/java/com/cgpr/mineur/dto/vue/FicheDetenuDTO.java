package com.cgpr.mineur.dto.vue;

 

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FicheDetenuDTO {
    private String idDetenue;
    private String nomComplet;
    private String dateLieuNaissance;
    private Integer age;
    private String adresseComplete;
    private String formationMetier;
    private String situationFamilialeSociale;
    private String nationalite;
    private String classePenale;
    private Long numeroOrdArr;
    private Long numeroOrdRes;
    private String numeroArrestation;
    private String nomEtablissement;
    private String decision;
    private Date dateArrestation;
    private Date dateDebutPunition;
    private Date dateFinPunition;
    private List<AffaireDTO> affaires; // Liste des affaires associées au détenu
}