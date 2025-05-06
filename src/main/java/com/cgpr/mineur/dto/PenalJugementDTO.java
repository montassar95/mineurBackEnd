package com.cgpr.mineur.dto;

import java.time.LocalDate;
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
public class PenalJugementDTO {

	private String tnumide;
    private String tcoddet;
    private String tnumseqaff;
    private String tcodextj;
    private String dateJugement;
    private String dateDepot;
    private String ttexjug;
    private String libelleTjugement;
    private String dateDebutPunition;
    private String dateFinPunition;
    
    private List <AccusationExtraitJugementDTO> accusationExtraitJugementDTOs;
}
