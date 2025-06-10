package com.cgpr.mineur.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

public class ArretExecutionPenalDTO {
 
	
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

	    private String codeDocument;
	    private String codeDocumentSecondaire;
	    private String typeActe;
	    
	    
	 private String dateActe;
	    private String numAffaire;
	    private String libelleTribunal;
	    private String typeDocument;
	    private String typeMotif;
	    private String libelleMotif;

 
}
