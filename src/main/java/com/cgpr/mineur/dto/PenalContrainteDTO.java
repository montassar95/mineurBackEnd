package com.cgpr.mineur.dto;

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
public class PenalContrainteDTO {
	private String tnumide;
    private String tcoddet;
    private String tnumseqaff;
    private String firstname;
    private String motherName;
    private String birthDate;
    private String adresse;
    private String numeroEcrou;
    private String prision;
    private String codeDocument;
    private String dateJugement;
    private String dateDepot;
    private String numAffaire;
    private String libelleTribunal;
    private String libelleFamilleAcc;

    private String montantAmende;
    private String fraisJudiciaires;
    private String versementsPayes;
    private String joursComptabilises;
    private String resteDu;
    private String dureeAffaire;
    private String dateRevisionJbr;
    private String dureeApresRevision;
    private String dateDebutPeine;
    private String dateFinPeine;

}
