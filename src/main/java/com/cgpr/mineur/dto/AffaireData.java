package com.cgpr.mineur.dto;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AffaireData {

    private String idEnfant;
    private ArrestationDto arrestation;
    private String numAffaire1;
    private TribunalDto tribunal1;
    private String numAffaire2;
    private TribunalDto tribunal2;
    private AffaireDto affaireOrigine;
}