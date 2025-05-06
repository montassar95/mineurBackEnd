package com.cgpr.mineur.controllers;

import org.springframework.stereotype.Service;

import com.cgpr.mineur.config.DetenuStatutDTO;
import com.cgpr.mineur.repository.rapport.DetenuRepositoryCustom;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class DetenuService {

    private final DetenuRepositoryCustom detenuRepositoryCustom;

    public DetenuService(DetenuRepositoryCustom detenuRepositoryCustom) {
        this.detenuRepositoryCustom = detenuRepositoryCustom;
    }

    public List<DetenuStatutDTO> getFilteredDetenus(LocalDate dateDebut, LocalDate dateFin ,  String etablissementReside, String situationJudiciaireId) throws IOException {
        return detenuRepositoryCustom.findDetenuStatut(dateDebut, dateFin ,    etablissementReside,   situationJudiciaireId);
    }
}

