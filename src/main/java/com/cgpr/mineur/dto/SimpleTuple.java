package com.cgpr.mineur.dto;

public class SimpleTuple {
    private final String idEnfant;
    private final long numOrdinale;

    public SimpleTuple(String idEnfant, long numOrdinale) {
        this.idEnfant = idEnfant;
        this.numOrdinale = numOrdinale;
    }

    public String getIdEnfant() {
        return idEnfant;
    }

    public long getNumOrdinale() {
        return numOrdinale;
    }
}