package com.cgpr.mineur.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.dto.DecesDto;
import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.RapportQuotidien;
import com.cgpr.mineur.models.Residence;
import com.cgpr.mineur.repository.RapportQuotidienRepository;
import com.cgpr.mineur.service.DecesService;
import com.cgpr.mineur.serviceReporting.ChargeAllEnfantService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/chargerList")
public class ChargerListController {

    @Autowired
    private ChargeAllEnfantService chargeAllEnfantService;

    @Autowired
    private RapportQuotidienRepository rapportQuotidienRepository;

    @GetMapping("/all")
    public void listCharge() {
        System.out.println("debut");

        ObjectMapper objectMapper = new ObjectMapper();
        String residenceJson = null;

        try {
            // Assurez-vous que chargeList() retourne une copie immuable.
//            List<List<Residence>> residences = chargeAllEnfantService.chargeList();
            
          
            

            // Sérialisation
            residenceJson = objectMapper.writeValueAsString( chargeAllEnfantService.chargeList());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        RapportQuotidien rapportQuotidien = new RapportQuotidien();
        rapportQuotidien.setDateSauvgarde(LocalDateTime.now());
        rapportQuotidien.setListResidance(residenceJson.toString());

        // Enregistrer  
        rapportQuotidienRepository.save(rapportQuotidien);

        System.out.println("fin... All");
    }
}


