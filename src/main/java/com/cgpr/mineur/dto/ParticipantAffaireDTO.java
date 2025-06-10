package com.cgpr.mineur.dto;

import java.sql.Date;

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
public class ParticipantAffaireDTO {
	private String tnumide;
    private String tcoddet;
    private String firstname;
    private String fatherName;
    private String grandfatherName;
    private String lastname;
    private String birthDate;
    private String motherName;
    private String maternalGrandmotherName;
    private String adresse;
    private String tnumjafFormatte2;
    private String libelleTribunal;
    private String libelleNature;
}
