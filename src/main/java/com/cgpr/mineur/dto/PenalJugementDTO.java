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

	//private String tnumide;
  //  private String tcoddet;
 //   private String tnumseqaff;
  //  private String tcodextj;
	
	 // Identifiants
    private String tnumide;
    private String tcoddet;
    private String tnumseqaff;

    // Informations personnelles
    private String firstname; // Nom complet (nom + père + grand-père)
    private String motherName; // Mère + grand-mère
    private String birthDate;
    private String adresse;

    // Informations de détention
    private String numeroEcrou;
    private String prision;

    // Mandat
    private String codeDocument;
    
    private String numAffaire;
    private String libelleTribunal;
    
    private String dateJugement;
    private String dateDepot;
    private String ttexjug;
    private String libelleTjugement;
    private String dateDebutPunition;
    private String dateFinPunition;
    
   private int periodeAnnee;
   private int periodeMois;
   private int periodeJour;
    private List <AccusationExtraitJugementDTO> accusationExtraitJugementDTOs;
}
