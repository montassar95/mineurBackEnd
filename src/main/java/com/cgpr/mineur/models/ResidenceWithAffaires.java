package com.cgpr.mineur.models;

import java.util.List;

public class ResidenceWithAffaires {
    private Residence residence;
    private List<Affaire> affaires;

    public ResidenceWithAffaires(Residence residence, List<Affaire> affaires) {
        this.residence = residence;
        this.affaires = affaires;
    }

    // Getters et Setters
    public Residence getResidence() {
        return residence;
    }

    public void setResidence(Residence residence) {
        this.residence = residence;
    }

    public List<Affaire> getAffaires() {
        return affaires;
    }

    public void setAffaires(List<Affaire> affaires) {
        this.affaires = affaires;
    }
}

