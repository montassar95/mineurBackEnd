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
public class PenalTransfertDto  {

	   private String tnumide;
	    private String tcoddet;
	    private String tnumseqaff;

	    private String firstname;
	    private String motherName;
	    private String birthDate;
	    private String adresse;

	    private String codeDocument;
	    private String dateTransfert;

	    private String numAffaireDepart;
	    private String libelleTribunalDepart;

	    private String numAffaireArrivee;
	    private String libelleTribunalArrivee;
	    private String typeTransfert;
}

 