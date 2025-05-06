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
public class AffaireDto {

 
	private AffaireIdDto affaireId;

	 
	private ArrestationDto arrestation;

	private long numOrdinalAffaire;

	private long numOrdinalAffaireByAffaire;

	 
	private TribunalDto tribunal;

	 
	private AffaireDto affaireLien;

	 
	private AffaireDto affaireAffecter;

	private int statut;

	 
	private List<DocumentDto> documents;

	 
	private List<TitreAccusationDto> titreAccusations;

	private String typeDocument;

	private String typeDocumentActuelle;

	private Date dateEmissionDocument;

	 
	private TypeAffaireDto typeAffaire;

	 
	private int jour;
	 
	private int mois;
	 
	private int annee;

	 
	private int jourArret;
	 
	private int moisArret;
	 
	private int anneeArret;

	 
	private String typeFile;

	 
	private TypeJugeDto typeJuge;

	 
 	private Date dateEmission;

	private Date dateDebutPunition;

	private Date dateFinPunition;
	private long daysDiffJuge;

	 
     private boolean affairePrincipale;

}
