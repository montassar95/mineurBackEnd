package com.cgpr.mineur.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.*;

import com.cgpr.mineur.config.Simplification;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "enf")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Enfant implements Serializable {

    @Id
    private String id;
    private String nom;
    private String prenom;

    private String nomPere;
    private String nomGrandPere;
    private String nomMere;
    private String prenomMere;
    
    //  @JsonIgnore
    @Column(name = "DATE_NAISSANCE")
    private LocalDate dateNaissance;
    
  
    
    
    private String lieuNaissance;
    private String sexe;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
//    @MapsId
    @JoinColumn(name = "simplifier_criteria_id")
    private SimplifierCriteria simplifierCriteria;

    @ManyToOne
    private Nationalite nationalite;

    @ManyToOne
    private NiveauEducatif niveauEducatif;

    @ManyToOne
    private SituationFamiliale situationFamiliale;

    private int nombreFreres;

    @ManyToOne
    private Gouvernorat gouvernorat;

    @ManyToOne
    private Delegation delegation;

    private String adresse;
    private String surnom;
    private String alias;

    @ManyToOne
    private ClassePenale classePenale;

    @ManyToOne
    private SituationSocial situationSocial;

    @ManyToOne
    private Metier metier;

//    @Transient
//    private String etat;

    private int nbrEnfant;

    @PrePersist
    public void prePersist() {
    	Simplification simplifier = new Simplification();
        String nomSimplifie = simplifier.simplify(nom);
        String prenomSimplifie = simplifier.simplify(prenom);
        String nomPereSimplifie = simplifier.simplify(nomPere);
        String nomGrandPereSimplifie = simplifier.simplify(nomGrandPere);
        String nomMereSimplifie = simplifier.simplify(nomMere);
        String prenomMereSimplifie = simplifier.simplify(prenomMere);

       

        SimplifierCriteria simp = new SimplifierCriteria();
        simp.setSimplifierId(id);
        simp.setNomSimplifie(nomSimplifie);
        simp.setPrenomSimplifie(prenomSimplifie);
        simp.setNomPereSimplifie(nomPereSimplifie);
        simp.setNomMereSimplifie(nomMereSimplifie);
        simp.setPrenomMereSimplifie(prenomMereSimplifie);
        simp.setNomGrandPereSimplifie(nomGrandPereSimplifie);
        simp.setDateNaissance(dateNaissance);
        
        simp.setLieuNaissance(simplifier.simplify(lieuNaissance));
        // Simplification du sexe
        if ("ذكر".equals(this.sexe)) {
        	simp.setSexe("1");
        } else if ("أنثى".equals(this.sexe)) {
        	simp.setSexe("0");
        } else {
        	simp.setSexe(null);
        }
        this.simplifierCriteria = simp;
        System.err.println(simplifierCriteria.toString());
    }
}
