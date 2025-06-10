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
public class PenalContestationDto  {

	 private String tnumide;
	    private String tcoddet;
	    private String tnumseqaff;
	    private String firstname;
	    private String motherName;
	    private String birthDate;
	    private String adresse;
	    private String codeDocument;
	    private String  codeDocumentJugement;
	    private String dateContestation;
	    private String numAffaire;
	    private String libelleTribunal;
	    private String libelleContestation;
}
