package com.cgpr.mineur.controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.config.DetenuStatutDTO;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class DetenuController {

    private final DetenuService detenuService;

    public DetenuController(DetenuService detenuService) {
        this.detenuService = detenuService;
    }

 
    @GetMapping(value = "/detenus/statuts", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DetenuStatutDTO> getDetenus(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate dateFin,
            @RequestParam(required = false)      String etablissementReside,
            @RequestParam(required = false)       String situationJudiciaireId
    ) throws IOException { 
        return detenuService.getFilteredDetenus(dateDebut, dateFin ,    etablissementReside,   situationJudiciaireId);
    }
}

