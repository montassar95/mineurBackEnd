package com.cgpr.mineur.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentSearchCriteriaDto {
    private String idEnfant;
    private long numOrdinalArrestation;
    private long numOrdinalAffaire;
}
